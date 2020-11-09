package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.BookItemEntity;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.mapper.*;
import com.sahin.library_management.repository.BookItemRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Book item service tests:")
class BookItemServiceTest {

    @Configuration
    @EnableCaching
    static class SpringConfig {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache("bookItems")
            ));
            return cacheManager;
        }

        @Bean
        public BookItemService bookItemService() {
            return new BookItemService();
        }

        @Bean
        public BookItemRepository itemRepository() {
            return mock(BookItemRepository.class);
        }

        @Bean
        public BookItemMapper bookItemMapper() {
            return new BookItemMapperImpl();
        }

        @Bean
        public BookMapper bookMapper() {
            return new BookMapperImpl();
        }

        @Bean
        public RackMapper rackMapper() {
            return new RackMapperImpl();
        }

        @Bean
        public BookCategoryMapper categoryMapper() {
            return new BookCategoryMapperImpl();
        }

        @Bean
        public AuthorMapper authorMapper() {
            return new AuthorMapperImpl();
        }
    }

    @Nested
    @DisplayName("Caching:")
    @ContextConfiguration(classes = BookItemServiceTest.SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingTest {

        @Autowired
        private BookItemService itemService;

        @Autowired
        private BookItemRepository itemRepository;

        @Autowired
        private CacheManager cacheManager;

        private BookItemEntity itemEntity;
        private BookItem item;
        private Cache itemsCache;

        @BeforeAll
        void setup() {
            itemsCache = cacheManager.getCache("bookItems");

            itemEntity = new BookItemEntity();
            itemEntity.setBarcode(UUID.randomUUID().toString());

            item = new BookItem();
            item.setBarcode(itemEntity.getBarcode());
        }

        @BeforeEach
        void resetSetup() {
            itemsCache.clear();
            reset(itemRepository);
        }

        @Test
        @DisplayName("Item is removed from cache by barcode while updating it")
        void updateCacheWhenUpdatingItem() {

            BookItemEntity updatedEntity = new BookItemEntity();
            updatedEntity.setBarcode(itemEntity.getBarcode());
            updatedEntity.setStatus(BookStatus.LOANED);

            given(itemRepository.findById(anyString())).willReturn(Optional.of(itemEntity));
            given(itemRepository.save(any())).willReturn(updatedEntity);

            // put old entity to the cache
            itemsCache.put(item.getBarcode(), item);
            itemService.updateBookItem(item);

            BookItem cachedResult = (BookItem) itemsCache.get(item.getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(updatedEntity.getBarcode(), cachedResult.getBarcode()),
                    () -> assertEquals(updatedEntity.getStatus(), cachedResult.getStatus())
            );
        }

        @Test
        @DisplayName("Item is removed from cache by barcode while deleting it")
        void deleteFromCacheWhenDeletingItem() {
            given(itemRepository.findById(anyString())).willReturn(Optional.of(itemEntity));
            doNothing().when(itemRepository).deleteById(anyString());

            itemsCache.put(item.getBarcode(), item);
            itemService.deleteBookItemByBarcode(item.getBarcode());

            assertNull(itemsCache.get(item.getBarcode()));
        }

        @Test
        @DisplayName("Item is cached by barcode while getting it")
        void cacheWhenGettingByBarcode() {

            given(itemRepository.findById(anyString())).willReturn(Optional.of(itemEntity));

            itemService.getBookItemByBarcode(itemEntity.getBarcode());
            itemService.getBookItemByBarcode(itemEntity.getBarcode());

            verify(itemRepository, times(1)).findById(anyString());
        }

        @Test
        @DisplayName("Items are put into cache while getting all of them")
        void putIntoCacheWhenGettingByBookId() {
            given(itemRepository.findByBookId(anyLong())).willReturn(Arrays.asList(itemEntity));

            itemService.getBookItemByBookId(anyLong());

            BookItem cachedResult = (BookItem) itemsCache.get(item.getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(item.getBarcode(), cachedResult.getBarcode())
            );
        }
    }
}
