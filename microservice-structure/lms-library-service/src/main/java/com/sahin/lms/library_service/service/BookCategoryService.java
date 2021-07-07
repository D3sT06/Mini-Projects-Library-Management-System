package com.sahin.lms.library_service.service;

import com.sahin.lms.infra.annotation.LogExecutionTime;
import com.sahin.lms.infra.entity.library.jpa.BookCategoryEntity;
import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.infra.mapper.BookCategoryMapper;
import com.sahin.lms.infra.mapper.CyclePreventiveContext;
import com.sahin.lms.infra.model.book.BookCategory;
import com.sahin.lms.infra.projections.CategoryProjections;
import com.sahin.lms.library_service.repository.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
@CacheConfig(cacheNames = "categories")
public class BookCategoryService {
    @Autowired
    private BookCategoryRepository categoryRepository;

    @Autowired
    private BookCategoryMapper categoryMapper;

    @Resource
    private BookCategoryService self;

    @Transactional
    public BookCategory createCategory(BookCategory category) {
        if (category.getId() != null)
            throw new MyRuntimeException("NOT CREATED", "Category to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        BookCategoryEntity entity = categoryMapper.toEntity(category, new CyclePreventiveContext());
        entity = categoryRepository.save(entity);
        return categoryMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public void updateCategory(BookCategory category) {
        if (category.getId() == null)
            throw new MyRuntimeException("NOT UPDATED", "Category to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!categoryRepository.findById(category.getId()).isPresent())
            throw setExceptionWhenCategoryNotExist(category.getId());

        BookCategoryEntity entity = categoryMapper.toEntity(category, new CyclePreventiveContext());
        entity = categoryRepository.save(entity);
        self.cacheEvict(entity);
    }

    @Transactional
    @CacheEvict(key = "#categoryId")
    public void deleteCategoryById(Long categoryId) {
        Optional<BookCategoryEntity> optionalEntity = categoryRepository.findById(categoryId);

        if (!optionalEntity.isPresent())
            throw setExceptionWhenCategoryNotExist(categoryId);

        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    @Cacheable(key = "#categoryId")
    public CategoryProjections.CategoryView getCategoryById(Long categoryId) {
        return categoryRepository
                .findProjectedById(categoryId)
                .orElseThrow(()-> setExceptionWhenCategoryNotExist(categoryId));
    }

    @Transactional
    public List<CategoryProjections.CategoryView> getAll() {

        List<CategoryProjections.CategoryView> categoryViews = categoryRepository.findAllProjectedBy();

        for (CategoryProjections.CategoryView view : categoryViews)
            self.cache(view);

        return categoryViews;
    }

    @CachePut(key = "#view.id")
    public CategoryProjections.CategoryView cache(CategoryProjections.CategoryView view) {
        return view;
    }

    @CacheEvict(key = "#entity.id")
    public void cacheEvict(BookCategoryEntity entity) {}

    private MyRuntimeException setExceptionWhenCategoryNotExist(Long categoryId) {
        return new MyRuntimeException("NOT FOUND", "Category with id \"" + categoryId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }
}
