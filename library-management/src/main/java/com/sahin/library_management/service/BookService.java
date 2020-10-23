package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.search.BookFilter;
import com.sahin.library_management.infra.model.search.BookSpecification;
import com.sahin.library_management.mapper.BookMapper;
import com.sahin.library_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Transactional
    public List<Book> searchBook(BookFilter bookFilter) {
        Specification<BookEntity> specification = BookSpecification.create(bookFilter);
        List<BookEntity> entities = bookRepository.findAll(specification);
        return bookMapper.toModels(entities);
    }

    @Transactional
    public Book createBook(Book book) {
        if (book.getId() != null)
            throw new MyRuntimeException("NOT CREATED", "Book to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        BookEntity entity = bookMapper.toEntity(book);
        entity = bookRepository.save(entity);
        return bookMapper.toModel(entity);
    }

    @Transactional
    public void updateBook(Book book) {
        if (book.getId() == null)
            throw new MyRuntimeException("NOT UPDATED", "Book to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!bookRepository.findById(book.getId()).isPresent())
            throw setExceptionWhenBookNotExist(book.getId());

        BookEntity entity = bookMapper.toEntity(book);
        bookRepository.save(entity);
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
        BookEntity entity = bookRepository
                .findById(bookId)
                .orElseThrow(()-> setExceptionWhenBookNotExist(bookId));

        return bookMapper.toModel(entity);
    }

    private MyRuntimeException setExceptionWhenBookNotExist(Long bookId) {
        return new MyRuntimeException("NOT FOUND", "Book with id \"" + bookId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }
}