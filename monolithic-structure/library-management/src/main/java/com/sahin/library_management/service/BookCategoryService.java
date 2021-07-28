package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.repository.jpa.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookCategoryService {

    @Autowired
    private BookCategoryRepository categoryRepository;

    @Transactional
    public BookCategoryEntity createCategory(BookCategoryEntity category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(BookCategoryEntity category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public BookCategoryEntity getCategoryById(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .get();
    }

    @Transactional
    public List<BookCategoryEntity> getAll() {
        return categoryRepository.findAll();
    }
}
