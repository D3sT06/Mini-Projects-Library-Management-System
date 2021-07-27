package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import com.sahin.library_management.mapper.BookCategoryMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookCategoryService {

    @Autowired
    private BookCategoryRepository categoryRepository;

    @Autowired
    private BookCategoryMapper categoryMapper;

    @Transactional
    public BookCategory createCategory(BookCategory category) {
        BookCategoryEntity entity = categoryMapper.toEntity(category, new CyclePreventiveContext());
        entity = categoryRepository.save(entity);
        return categoryMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public void updateCategory(BookCategory category) {
        BookCategoryEntity entity = categoryMapper.toEntity(category, new CyclePreventiveContext());
        categoryRepository.save(entity);
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public BookCategory getCategoryById(Long categoryId) {
        BookCategoryEntity entity = categoryRepository
                .findById(categoryId)
                .get();

        return categoryMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public List<BookCategory> getAll() {
        List<BookCategoryEntity> entities = categoryRepository.findAll();
        return categoryMapper.toModelsList(entities, new CyclePreventiveContext());
    }
}
