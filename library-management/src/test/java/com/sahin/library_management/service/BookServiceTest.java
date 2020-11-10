package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.mapper.*;
import com.sahin.library_management.repository.BookRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@DisplayName("Book service tests:")
class BookServiceTest {

    @Configuration
    @EnableCaching
    static class SpringConfig {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache("books")
            ));
            return cacheManager;
        }

        @Bean
        public BookService bookService() {
            return new BookService();
        }

        @Bean
        public BookRepository bookRepository() {
            return mock(BookRepository.class);
        }

        @Bean
        public BookMapper bookMapper() {
            return new BookMapperImpl();
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
    @DisplayName("Caching Repeated Tests:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingRepeatedTests {

        @Autowired
        private BookService bookService;

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private CacheManager cacheManager;

        private BookEntity bookEntity;
        private Cache booksCache;

        @BeforeAll
        void setup() {
            booksCache = cacheManager.getCache("books");

            bookEntity = new BookEntity();
            bookEntity.setId(1L);
        }

        @BeforeEach
        void resetSetup(RepetitionInfo repetitionInfo) {
            if (repetitionInfo.getCurrentRepetition() != 1)
                return;

            booksCache.clear();
            reset(bookRepository);
        }

        @RepeatedTest(2)
        @DisplayName("Book is cached by id while getting it")
        void cacheWhenGettingByBarcode() {

            given(bookRepository.findById(anyLong())).willReturn(Optional.of(bookEntity));

            bookService.getBookById(bookEntity.getId());

            verify(bookRepository, times(1)).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("Caching:")
    @ContextConfiguration(classes = SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingTest {

        @Autowired
        private BookService bookService;

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private CacheManager cacheManager;

        private BookEntity bookEntity;
        private Book book;
        private Cache booksCache;

        @BeforeAll
        void setup() {
            booksCache = cacheManager.getCache("books");

            bookEntity = new BookEntity();
            bookEntity.setId(1L);

            book = new Book();
            book.setId(bookEntity.getId());
        }

        @BeforeEach
        void resetSetup() {
            booksCache.clear();
            reset(bookRepository);
        }

        @Test
        @DisplayName("Book is put into cache by id while updating it")
        void updateCacheWhenUpdating() {

            BookEntity updatedEntity = new BookEntity();
            updatedEntity.setId(bookEntity.getId());
            updatedEntity.setTitle("new");

            given(bookRepository.findById(anyLong())).willReturn(Optional.of(bookEntity));
            given(bookRepository.save(any())).willReturn(updatedEntity);

            // put old entity to the cache
            booksCache.put(book.getId(), book);
            bookService.updateBook(book);

            Book cachedResult = (Book) booksCache.get(book.getId()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(updatedEntity.getId(), cachedResult.getId()),
                    () -> assertEquals(updatedEntity.getTitle(), cachedResult.getTitle())
            );
        }

        @Test
        @DisplayName("Book is removed from cache by id while deleting it")
        void deleteFromCacheWhenDeleting() {
            given(bookRepository.findById(anyLong())).willReturn(Optional.of(bookEntity));
            doNothing().when(bookRepository).deleteById(anyLong());

            booksCache.put(book.getId(), book);
            bookService.deleteBookById(book.getId());

            assertNull(booksCache.get(book.getId()));
        }


        @Test
        @DisplayName("Books are put into cache while getting all of them")
        void putIntoCacheWhenSearching() {

            given(bookRepository.findAll(any(), (Pageable) any())).willReturn(
                    new PageImpl<>(Arrays.asList(bookEntity), PageRequest.of(0, 10), 1)
            );

            bookService.searchBook(any(), any());

            Book cachedResult = (Book) booksCache.get(book.getId()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(book.getId(), cachedResult.getId())
            );
        }
    }
}
