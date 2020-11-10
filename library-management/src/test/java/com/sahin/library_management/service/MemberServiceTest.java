package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.mapper.*;
import com.sahin.library_management.repository.AccountRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Member service tests:")
class MemberServiceTest {

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
        public MemberService memberService() {
            return new MemberService();
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
        private MemberService memberService;

        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private LibraryCardMapper cardMapper;

        @Autowired
        private CacheManager cacheManager;

        private Member member;
        private AccountEntity accountEntity;
        private Cache accountsCache;

        @BeforeAll
        void setup() {
            accountsCache = cacheManager.getCache("accounts");

            LibraryCard card = new LibraryCard();
            card.setBarcode(UUID.randomUUID().toString());
            card.setAccountFor(AccountFor.MEMBER);

            member = new Member();
            member.setId(1L);
            member.setLibraryCard(card);

            accountEntity = new AccountEntity();
            accountEntity.setId(member.getId());
            accountEntity.setLibraryCard(cardMapper.toEntity(member.getLibraryCard(), new CyclePreventiveContext()));
        }

        @BeforeEach
        void resetSetup(RepetitionInfo repetitionInfo) {
            if (repetitionInfo.getCurrentRepetition() != 1)
                return;

            accountsCache.clear();
            reset(accountRepository);
        }

        @RepeatedTest(2)
        @DisplayName("Member is cached by id while getting it")
        void cacheWhenGettingByBarcode() {

            given(accountRepository.findByLibraryCardBarcode(anyString())).willReturn(Optional.of(accountEntity));

            memberService.getMemberByBarcode(member.getLibraryCard().getBarcode());

            verify(accountRepository, times(1)).findByLibraryCardBarcode(anyString());
        }
    }

    @Nested
    @DisplayName("Caching:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingTest {

        @Autowired
        private MemberService memberService;

        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private LibraryCardMapper cardMapper;

        @Autowired
        private CacheManager cacheManager;

        private Member member;
        private AccountEntity accountEntity;
        private Cache accountsCache;

        @BeforeAll
        void setup() {
            accountsCache = cacheManager.getCache("accounts");

            LibraryCard card = new LibraryCard();
            card.setBarcode(UUID.randomUUID().toString());
            card.setAccountFor(AccountFor.MEMBER);

            member = new Member();
            member.setId(1L);
            member.setLibraryCard(card);

            accountEntity = new AccountEntity();
            accountEntity.setId(member.getId());
            accountEntity.setLibraryCard(cardMapper.toEntity(member.getLibraryCard(), new CyclePreventiveContext()));
        }

        @BeforeEach
        void resetSetup() {
            accountsCache.clear();
            reset(accountRepository);
        }

        @Test
        @DisplayName("Member is put into cache by barcode while creating it")
        void updateCacheWhenCreating() {

            Member memberToBeCreated = new Member();

            given(accountRepository.save(any())).willReturn(accountEntity);

            memberService.createMember(memberToBeCreated);

            Member cachedResult = (Member) accountsCache.get(accountEntity.getLibraryCard().getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(accountEntity.getId(), cachedResult.getId())
            );
        }

        @Test
        @DisplayName("Member is put into cache by barcode while updating it")
        void updateCacheWhenUpdating() {

            AccountEntity updatedEntity = new AccountEntity();
            updatedEntity.setId(accountEntity.getId());
            updatedEntity.setLibraryCard(accountEntity.getLibraryCard());
            updatedEntity.setName("new");

            given(accountRepository.findById(anyLong())).willReturn(Optional.of(accountEntity));
            given(accountRepository.save(any())).willReturn(updatedEntity);

            // put old entity to the cache
            accountsCache.put(member.getId(), member);
            memberService.updateMember(member);

            Member cachedResult = (Member) accountsCache.get(member.getLibraryCard().getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(updatedEntity.getId(), cachedResult.getId()),
                    () -> assertEquals(updatedEntity.getName(), cachedResult.getName())
            );
        }

        @Test
        @DisplayName("Member is removed from cache by barcode while deleting it")
        void deleteFromCacheWhenDeleting() {
            given(accountRepository.findByLibraryCardBarcode(anyString())).willReturn(Optional.of(accountEntity));
            doNothing().when(accountRepository).deleteByLibraryCardBarcode(anyString());

            accountsCache.put(member.getLibraryCard().getBarcode(), member);
            memberService.deleteMemberByBarcode(member.getLibraryCard().getBarcode());

            assertNull(accountsCache.get(member.getId()));
        }


        @Test
        @DisplayName("Members are put into cache while getting all of them")
        void putIntoCacheWhenGettingAll() {

            given(accountRepository.getAll((AccountFor) any())).willReturn(Arrays.asList(accountEntity));

            memberService.getAll();

            Member cachedResult = (Member) accountsCache.get(member.getLibraryCard().getBarcode()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(member.getId(), cachedResult.getId())
            );
        }
    }
}
