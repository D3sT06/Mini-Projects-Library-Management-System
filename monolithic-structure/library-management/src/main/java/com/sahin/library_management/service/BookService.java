package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private LibraryRepository libraryRepository;

    public void createBook(BookEntity book) {
        libraryRepository.save(book);
    }

    public void updateBook(BookEntity book) {
        libraryRepository.update(book);
    }

    public void deleteBookById(String barcode) {
        libraryRepository.deleteById(barcode, BookEntity.class);
    }

    public BookEntity getBookById(String barcode) {
        return (BookEntity) libraryRepository
                .findById(barcode, BookEntity.class);
    }

    public List<BookEntity> getAll() {
        return libraryRepository.findAll(BookEntity.class)
                .stream()
                .map(entity -> (BookEntity) entity)
                .collect(Collectors.toList());
    }
}