package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.repository.LibraryRepository;
import com.sahin.library_management.repository.jpa.jpa.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookCategoryService {

    @Autowired
    private LibraryRepository libraryRepository;

    public BookCategoryEntity createCategory(BookCategoryEntity category) {
        return categoryRepository.save(category);
    }

    public void updateCategory(BookCategoryEntity category) {
        categoryRepository.save(category);
    }

    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public BookCategoryEntity getCategoryById(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .get();
    }

    public List<BookCategoryEntity> getAll() {
        return categoryRepository.findAll();
    }
}
