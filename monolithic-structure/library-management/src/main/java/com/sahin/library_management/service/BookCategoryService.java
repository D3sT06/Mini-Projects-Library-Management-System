package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCategoryService {

    @Autowired
    private LibraryRepository libraryRepository;

    public void createCategory(BookCategoryEntity category) {
        libraryRepository.save(category);
    }

    public void updateCategory(BookCategoryEntity category) {
        libraryRepository.update(category);
    }

    public void deleteCategoryById(String barcode) {
        libraryRepository.deleteById(barcode, BookCategoryEntity.class);
    }

    public BookCategoryEntity getCategoryById(String barcode) {
        return (BookCategoryEntity) libraryRepository
                .findById(barcode, BookCategoryEntity.class);
    }

    public List<BookCategoryEntity> getAll() {
        return libraryRepository.findAll(BookCategoryEntity.class)
                .stream()
                .map(entity -> (BookCategoryEntity) entity)
                .collect(Collectors.toList());
    }
}
