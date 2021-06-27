package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.infra.entity.jpa.BookEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import com.sahin.library_management.infra.projections.CategoryProjections;
import com.sahin.library_management.mapper.BookCategoryMapper;
import com.sahin.library_management.mapper.BookCategoryMapperImpl;
import com.sahin.library_management.repository.jpa.BookCategoryRepository;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Category service tests:")
class BookCategoryServiceTest {

    @Configuration
    @EnableCaching
    static class SpringConfig {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache("categories")
            ));
            return cacheManager;
        }

        @Bean
        public BookCategoryService categoryService() {
            return new BookCategoryService();
        }

        @Bean
        public BookCategoryRepository categoryRepository() {
            return mock(BookCategoryRepository.class);
        }

        @Bean
        public BookCategoryMapper categoryMapper() {
            return new BookCategoryMapperImpl();
        }
    }

    @Nested
    @DisplayName("Caching Repeated Tests:")
    @ContextConfiguration(classes = BookCategoryServiceTest.SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingRepeatedTests {

        @Autowired
        private BookCategoryService categoryService;

        @Autowired
        private BookCategoryRepository categoryRepository;

        @Autowired
        private CacheManager cacheManager;

        private CategoryProjections.CategoryView categoryView;
        private Cache categoriesCache;

        @BeforeAll
        void setup() {
            categoriesCache = cacheManager.getCache("categories");

            categoryView = new CategoryProjections.CategoryView() {
                @Override
                public Long getId() {
                    return 1L;
                }

                @Override
                public @NotNull String getName() {
                    return "category";
                }

                @Override
                public @NotNull Set<BookEntity> getBooks() {
                    return null;
                }
            };
        }

        @BeforeEach
        void resetSetup(RepetitionInfo repetitionInfo) {
            if (repetitionInfo.getCurrentRepetition() != 1)
                return;

            categoriesCache.clear();
            reset(categoryRepository);
        }

        @RepeatedTest(2)
        @DisplayName("Category is cached by id while getting it")
        void cacheWhenGettingById() {

            given(categoryRepository.findProjectedById(anyLong())).willReturn(Optional.of(categoryView));

            categoryService.getCategoryById(1L);

            verify(categoryRepository, times(1)).findProjectedById(anyLong());
        }
    }

    @Nested
    @DisplayName("Caching:")
    @ContextConfiguration(classes = BookCategoryServiceTest.SpringConfig.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CachingTest {

        @Autowired
        private BookCategoryService categoryService;

        @Autowired
        private BookCategoryRepository categoryRepository;

        @Autowired
        private CacheManager cacheManager;

        private CategoryProjections.CategoryView categoryView;
        private BookCategoryEntity categoryEntity;
        private BookCategory category;
        private Cache categoriesCache;

        @BeforeAll
        void setup() {
            categoriesCache = cacheManager.getCache("categories");

            categoryEntity = new BookCategoryEntity();
            categoryEntity.setId(1L);

            category = new BookCategory();
            category.setId(1L);

            categoryView = new CategoryProjections.CategoryView() {
                @Override
                public Long getId() {
                    return 1L;
                }

                @Override
                public @NotNull String getName() {
                    return "category";
                }

                @Override
                public @NotNull Set<BookEntity> getBooks() {
                    return null;
                }
            };
        }

        @BeforeEach
        void resetSetup() {
            categoriesCache.clear();
            reset(categoryRepository);
        }

        @Test
        @DisplayName("Category is removed from cache by id while updating it")
        void deleteFromCacheWhenUpdatingAuthor() {
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(categoryEntity));
            given(categoryRepository.save(any())).willReturn(categoryEntity);

            categoriesCache.put(category.getId(), category);
            categoryService.updateCategory(category);

            assertNull(categoriesCache.get(category.getId()));
        }

        @Test
        @DisplayName("Category is removed from cache by id while deleting it")
        void deleteFromCacheWhenDeletingAuthor() {
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(categoryEntity));
            doNothing().when(categoryRepository).deleteById(anyLong());

            categoriesCache.put(category.getId(), category);
            categoryService.deleteCategoryById(category.getId());

            assertNull(categoriesCache.get(category.getId()));
        }

        @Test
        @DisplayName("Categories are put into cache while getting all of them")
        void putIntoCacheWhenGettingAll() {
            given(categoryRepository.findAllProjectedBy()).willReturn(Arrays.asList(categoryView));

            categoryService.getAll();

            CategoryProjections.CategoryView cachedResult = (CategoryProjections.CategoryView) categoriesCache.get(categoryView.getId()).get();

            assertAll(
                    () -> assertNotNull(cachedResult),
                    () -> assertEquals(categoryView.getId(), cachedResult.getId())
            );
        }
    }
}
