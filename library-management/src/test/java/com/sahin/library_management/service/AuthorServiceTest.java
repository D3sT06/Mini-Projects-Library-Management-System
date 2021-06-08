package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.entity.jpa.BookEntity;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.projections.AuthorProjections;
import com.sahin.library_management.mapper.AuthorMapper;
import com.sahin.library_management.mapper.AuthorMapperImpl;
import com.sahin.library_management.repository.jpa.AuthorRepository;
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

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Author service tests:")
class AuthorServiceTest {

    @Configuration
    @EnableCaching
    static class SpringConfig {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache("authors")
            ));
            return cacheManager;
        }

        @Bean
        public AuthorService authorService() {
            return new AuthorService();
        }

        @Bean
        public AuthorRepository authorRepository() {
            return mock(AuthorRepository.class);
        }

        @Bean
        public AuthorMapper authorMapper() {
            return new AuthorMapperImpl();
        }
    }

    @Nested
    @DisplayName("Caching Repeated Tests:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingRepeatedTests {
        @Autowired
        private AuthorService authorService;

        @Autowired
        private AuthorRepository authorRepository;

        @Autowired
        private CacheManager cacheManager;

        private AuthorProjections.AuthorView authorView;
        private Cache authorsCache;

        @BeforeEach
        void resetSetup(RepetitionInfo repetitionInfo) {

            if (repetitionInfo.getCurrentRepetition() != 1)
                return;

            authorsCache.clear();
            reset(authorRepository);
        }

        @BeforeAll
        void setup() {
            authorsCache = cacheManager.getCache("authors");

            authorView = new AuthorProjections.AuthorView() {
                @Override
                public Long getId() {
                    return 1L;
                }

                @Override
                public @NotNull String getName() {
                    return "name";
                }

                @Override
                public @NotNull String getSurname() {
                    return "surname";
                }

                @Override
                public String getFullname() {
                    return "name surname";
                }

                @Override
                public List<BookEntity> getBooks() {
                    return null;
                }
            };
        }

        @RepeatedTest(2)
        @DisplayName("Author is cached by id while getting it")
        void cacheWhenGettingById() {

            given(authorRepository.findProjectedById(anyLong())).willReturn(Optional.of(authorView));
            authorService.getAuthorById(1L);

            verify(authorRepository, times(1)).findProjectedById(anyLong());
        }
    }

    @Nested
    @DisplayName("Caching:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingTest {

        @Autowired
        private AuthorService authorService;

        @Autowired
        private AuthorRepository authorRepository;

        @Autowired
        private CacheManager cacheManager;

        private AuthorProjections.AuthorView authorView;
        private AuthorEntity authorEntity;
        private Author author;
        private Cache authorsCache;

        @BeforeAll
        void setup() {
            authorsCache = cacheManager.getCache("authors");

            authorEntity = new AuthorEntity();
            authorEntity.setId(1L);

            author = new Author();
            author.setId(1L);

            authorView = new AuthorProjections.AuthorView() {
                @Override
                public Long getId() {
                    return 1L;
                }

                @Override
                public @NotNull String getName() {
                    return "name";
                }

                @Override
                public @NotNull String getSurname() {
                    return "surname";
                }

                @Override
                public String getFullname() {
                    return "name surname";
                }

                @Override
                public List<BookEntity> getBooks() {
                    return null;
                }
            };
        }

        @BeforeEach
        void resetSetup() {
            authorsCache.clear();
            reset(authorRepository);
        }

        @Test
        @DisplayName("Author is removed from cache by id while updating it")
        void deleteFromCacheWhenUpdatingAuthor() {
            given(authorRepository.findById(anyLong())).willReturn(Optional.of(authorEntity));
            given(authorRepository.save(any())).willReturn(authorEntity);

            authorsCache.put(author.getId(), author);
            authorService.updateAuthor(author);

            assertNull(authorsCache.get(author.getId()));
        }

        @Test
        @DisplayName("Author is removed from cache by id while deleting it")
        void deleteFromCacheWhenDeletingAuthor() {
            given(authorRepository.findById(anyLong())).willReturn(Optional.of(authorEntity));
            doNothing().when(authorRepository).deleteById(anyLong());

            authorsCache.put(author.getId(), author);
            authorService.deleteAuthorById(author.getId());

            assertNull(authorsCache.get(author.getId()));
        }

        @Test
        @DisplayName("Authors are put into cache while getting all of them")
        void putIntoCacheWhenGettingAll() {
            given(authorRepository.findAllProjectedBy()).willReturn(Arrays.asList(authorView));

            authorService.getAll();

            AuthorProjections.AuthorView cachedResult = (AuthorProjections.AuthorView) authorsCache.get(authorView.getId()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(authorView.getId(), cachedResult.getId())
            );
        }


    }




}