package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.RackEntity;
import com.sahin.library_management.infra.model.book.Rack;
import com.sahin.library_management.mapper.RackMapper;
import com.sahin.library_management.mapper.RackMapperImpl;
import com.sahin.library_management.repository.RackRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Rack service tests:")
class RackServiceTest {

    @Configuration
    @EnableCaching
    static class SpringConfig {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache("racks")
            ));
            return cacheManager;
        }

        @Bean
        public RackService rackService() {
            return new RackService();
        }

        @Bean
        public RackRepository rackRepository() {
            return mock(RackRepository.class);
        }

        @Bean
        public RackMapper rackMapper() {
            return new RackMapperImpl();
        }
    }

    @Nested
    @DisplayName("Caching Repeated Tests:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingRepeatedTests {

        @Autowired
        private RackService rackService;

        @Autowired
        private RackRepository rackRepository;

        @Autowired
        private CacheManager cacheManager;

        private RackEntity rackEntity;
        private Cache racksCache;

        @BeforeAll
        void setup() {
            racksCache = cacheManager.getCache("racks");

            rackEntity = new RackEntity();
            rackEntity.setId(1L);
        }

        @BeforeEach
        void resetSetup(RepetitionInfo repetitionInfo) {
            if (repetitionInfo.getCurrentRepetition() != 1)
                return;

            racksCache.clear();
            reset(rackRepository);
        }

        @RepeatedTest(2)
        @DisplayName("Rack is cached by id while getting it")
        void cacheWhenGettingByBarcode() {

            given(rackRepository.findById(anyLong())).willReturn(Optional.of(rackEntity));

            rackService.getRackById(rackEntity.getId());

            verify(rackRepository, times(1)).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("Caching:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingTest {

        @Autowired
        private RackService rackService;

        @Autowired
        private RackRepository rackRepository;

        @Autowired
        private CacheManager cacheManager;

        private RackEntity rackEntity;
        private Rack rack;
        private Cache racksCache;

        @BeforeAll
        void setup() {
            racksCache = cacheManager.getCache("racks");

            rackEntity = new RackEntity();
            rackEntity.setId(1L);

            rack = new Rack();
            rack.setId(rackEntity.getId());
        }

        @BeforeEach
        void resetSetup() {
            racksCache.clear();
            reset(rackRepository);
        }

        @Test
        @DisplayName("Rack is put into cache by id while updating it")
        void updateCacheWhenUpdating() {

            RackEntity updatedEntity = new RackEntity();
            updatedEntity.setId(rackEntity.getId());
            updatedEntity.setLocation("new-1");

            given(rackRepository.findById(anyLong())).willReturn(Optional.of(rackEntity));
            given(rackRepository.save(any())).willReturn(updatedEntity);

            // put old entity to the cache
            racksCache.put(rack.getId(), rack);
            rackService.updateRack(rack);

            Rack cachedResult = (Rack) racksCache.get(rack.getId()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(updatedEntity.getId(), cachedResult.getId()),
                    () -> assertEquals(updatedEntity.getLocation(), cachedResult.getLocation())
            );
        }

        @Test
        @DisplayName("Rack is put into cache by id while creating it")
        void updateCacheWhenCreating() {

            Rack rackToBeCreated = new Rack();

            given(rackRepository.save(any())).willReturn(rackEntity);

            rackService.createRack(rackToBeCreated);

            Rack cachedResult = (Rack) racksCache.get(rackEntity.getId()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(rackEntity.getId(), cachedResult.getId())
            );
        }

        @Test
        @DisplayName("Rack is removed from cache by id while deleting it")
        void deleteFromCacheWhenDeleting() {
            given(rackRepository.findById(anyLong())).willReturn(Optional.of(rackEntity));
            doNothing().when(rackRepository).deleteById(anyLong());

            racksCache.put(rack.getId(), rack);
            rackService.deleteRackById(rack.getId());

            assertNull(racksCache.get(rack.getId()));
        }


        @Test
        @DisplayName("Racks are put into cache while getting all of them")
        void putIntoCacheWhenSearching() {

            given(rackRepository.findAll()).willReturn(Arrays.asList(rackEntity));

            rackService.getAll();

            Rack cachedResult = (Rack) racksCache.get(rack.getId()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(rack.getId(), cachedResult.getId())
            );
        }
    }
}
