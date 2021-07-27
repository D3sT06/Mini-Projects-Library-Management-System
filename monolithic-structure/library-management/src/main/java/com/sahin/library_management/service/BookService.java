package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.mapper.BookMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Transactional
    public Book createBook(Book book) {
        if (book.getId() != null)
            throw new MyRuntimeException("NOT CREATED", "Book to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        BookEntity entity = bookMapper.toEntity(book, new CyclePreventiveContext());
        entity = bookRepository.save(entity);
        return bookMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public Book updateBook(Book book) {
        if (book.getId() == null)
            throw new MyRuntimeException("NOT UPDATED", "Book to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!bookRepository.findById(book.getId()).isPresent())
            throw setExceptionWhenBookNotExist(book.getId());

        BookEntity entity = bookMapper.toEntity(book, new CyclePreventiveContext());
        entity = bookRepository.save(entity);
        return bookMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public void deleteBookById(Long bookId) {
        Optional<BookEntity> optionalEntity = bookRepository.findById(bookId);

        if (!optionalEntity.isPresent())
            throw setExceptionWhenBookNotExist(bookId);

        bookRepository.deleteById(bookId);
    }

    @Transactional
    public Book getBookById(Long bookId) {
        BookEntity bookEntity = bookRepository
                .findById(bookId)
                .orElseThrow(()-> setExceptionWhenBookNotExist(bookId));

        return bookMapper.toModel(bookEntity, new CyclePreventiveContext());
    }

    private MyRuntimeException setExceptionWhenBookNotExist(Long bookId) {
        return new MyRuntimeException("NOT FOUND", "Book with id \"" + bookId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }

    public List<Book> getAll() {
        List<BookEntity> books = (List<BookEntity>) bookRepository.findAll();
        return bookMapper.toModels(books, new CyclePreventiveContext());
    }
}