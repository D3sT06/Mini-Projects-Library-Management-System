package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.mapper.BookMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Transactional
    public Book createBook(Book book) {
        BookEntity entity = bookMapper.toEntity(book, new CyclePreventiveContext());
        entity = bookRepository.save(entity);
        return bookMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public Book updateBook(Book book) {
        BookEntity entity = bookMapper.toEntity(book, new CyclePreventiveContext());
        entity = bookRepository.save(entity);
        return bookMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Transactional
    public Book getBookById(Long bookId) {
        BookEntity bookEntity = bookRepository
                .findById(bookId)
                .get();

        return bookMapper.toModel(bookEntity, new CyclePreventiveContext());
    }

    public List<Book> getAll() {
        List<BookEntity> books = (List<BookEntity>) bookRepository.findAll();
        return bookMapper.toModels(books, new CyclePreventiveContext());
    }
}