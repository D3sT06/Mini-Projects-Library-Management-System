package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.repository.LibraryRepository;
import com.sahin.library_management.repository.jpa.jpa.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private LibraryRepository libraryRepository;

    public BookEntity createBook(BookEntity book) {
        return bookRepository.save(book);
    }

    public BookEntity updateBook(BookEntity book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public BookEntity getBookById(Long bookId) {
        return bookRepository
                .findById(bookId)
                .get();
    }

    public List<BookEntity> getAll() {
        return bookRepository.findAll();
    }
}