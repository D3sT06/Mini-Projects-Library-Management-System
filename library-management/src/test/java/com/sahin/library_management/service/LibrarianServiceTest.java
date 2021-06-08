package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.mapper.*;
import com.sahin.library_management.repository.jpa.AccountRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@DisplayName("Librarian service tests:")
class LibrarianServiceTest {

    @Configuration
    @EnableCaching
    static class SpringConfig {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache("accounts"),
                    new ConcurrentMapCache("libraryCards")
            ));
            return cacheManager;
        }

        @Bean
        public LibrarianService librarianService() {
            return new LibrarianService();
        }

        @Bean
        public AccountRepository accountRepository() {
            return mock(AccountRepository.class);
        }

        @Bean
        public AccountMapper accountMapper() {
            return new AccountMapperImpl();
        }

        @Bean
        public AccountLoginTypeMapper accountLoginTypeMapper() {
            return new AccountLoginTypeMapperImpl();
        }

        @Bean
        public LibraryCardMapper cardMapper() {
            return new LibraryCardMapperImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();
        }
    }

    @Nested
    @DisplayName("Caching Repeated Tests:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingRepeatedTests {

        @Autowired
        private LibrarianService librarianService;

        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private LibraryCardMapper cardMapper;

        @Autowired
        private CacheManager cacheManager;

        private Librarian librarian;
        private AccountEntity accountEntity;
        private Cache accountsCache;

        @BeforeAll
        void setup() {
            accountsCache = cacheManager.getCache("accounts");

            LibraryCard card = new LibraryCard();
            card.setBarcode(UUID.randomUUID().toString());
            card.setAccountFor(AccountFor.LIBRARIAN);

            librarian = new Librarian();
            librarian.setId(1L);
            librarian.setLibraryCard(card);

            accountEntity = new AccountEntity();
            accountEntity.setId(librarian.getId());
            accountEntity.setLibraryCard(cardMapper.toEntity(librarian.getLibraryCard(), new CyclePreventiveContext()));
        }

        @BeforeEach
        void resetSetup(RepetitionInfo repetitionInfo) {
            if (repetitionInfo.getCurrentRepetition() != 1)
                return;

            accountsCache.clear();
            reset(accountRepository);
        }

        @RepeatedTest(2)
        @DisplayName("Librarian is cached by id while getting it")
        void cacheWhenGettingByBarcode() {

            given(accountRepository.findByLibraryCardBarcode(anyString())).willReturn(Optional.of(accountEntity));

            librarianService.getLibrarianByBarcode(librarian.getLibraryCard().getBarcode());

            verify(accountRepository, times(1)).findByLibraryCardBarcode(anyString());
        }
    }

    @Nested
    @DisplayName("Caching:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingTest {

        @Autowired
        private LibrarianService librarianService;

        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private LibraryCardMapper cardMapper;

        @Autowired
        private CacheManager cacheManager;

        private Librarian librarian;
        private AccountEntity accountEntity;
        private Cache accountsCache;

        @BeforeAll
        void setup() {
            accountsCache = cacheManager.getCache("accounts");

            LibraryCard card = new LibraryCard();
            card.setBarcode(UUID.randomUUID().toString());
            card.setAccountFor(AccountFor.LIBRARIAN);

            librarian = new Librarian();
            librarian.setId(1L);
            librarian.setLibraryCard(card);

            accountEntity = new AccountEntity();
            accountEntity.setId(librarian.getId());
            accountEntity.setLibraryCard(cardMapper.toEntity(librarian.getLibraryCard(), new CyclePreventiveContext()));
        }

        @BeforeEach
        void resetSetup() {
            accountsCache.clear();
            reset(accountRepository);
        }

        @Test
        @DisplayName("Librarian is put into cache by barcode while creating it")
        void updateCacheWhenCreating() {

            Librarian librarianToBeCreated = new Librarian();

            given(accountRepository.save(any())).willReturn(accountEntity);

            librarianService.createLibrarian(librarianToBeCreated);

            Librarian cachedResult = (Librarian) accountsCache.get(accountEntity.getLibraryCard().getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(accountEntity.getId(), cachedResult.getId())
            );
        }

        @Test
        @DisplayName("Librarian is put into cache by barcode while updating it")
        void updateCacheWhenUpdating() {

            AccountEntity updatedEntity = new AccountEntity();
            updatedEntity.setId(accountEntity.getId());
            updatedEntity.setLibraryCard(accountEntity.getLibraryCard());
            updatedEntity.setName("new");

            given(accountRepository.findById(anyLong())).willReturn(Optional.of(accountEntity));
            given(accountRepository.save(any())).willReturn(updatedEntity);

            // put old entity to the cache
            accountsCache.put(librarian.getId(), librarian);
            librarianService.updateLibrarian(librarian);

            Librarian cachedResult = (Librarian) accountsCache.get(librarian.getLibraryCard().getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(updatedEntity.getId(), cachedResult.getId()),
                    () -> assertEquals(updatedEntity.getName(), cachedResult.getName())
            );
        }

        @Test
        @DisplayName("Librarian is removed from cache by barcode while deleting it")
        void deleteFromCacheWhenDeleting() {
            given(accountRepository.findByLibraryCardBarcode(anyString())).willReturn(Optional.of(accountEntity));
            doNothing().when(accountRepository).deleteByLibraryCardBarcode(anyString());

            accountsCache.put(librarian.getLibraryCard().getBarcode(), librarian);
            librarianService.deleteLibrarianByBarcode(librarian.getLibraryCard().getBarcode());

            assertNull(accountsCache.get(librarian.getId()));
        }


        @Test
        @DisplayName("Librarians are put into cache while getting all of them")
        void putIntoCacheWhenGettingAll() {

            given(accountRepository.getAll((AccountFor) any())).willReturn(Arrays.asList(accountEntity));

            librarianService.getAll();

            Librarian cachedResult = (Librarian) accountsCache.get(librarian.getLibraryCard().getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(librarian.getId(), cachedResult.getId())
            );
        }
    }
}
