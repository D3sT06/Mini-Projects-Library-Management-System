package com.sahin.lms.library_service.service;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_entity.library.jpa.BookEntity;
import com.sahin.lms.infra_exception.MyRuntimeException;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.library.model.Book;
import com.sahin.lms.infra_model.library.model.BookFilter;
import com.sahin.lms.library_service.mapper.BookMapper;
import com.sahin.lms.library_service.model.BookSpecification;
import com.sahin.lms.library_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
@CacheConfig(cacheNames = "books")
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Resource
    private BookService self;

    @Transactional
    public Page<Book> searchBook(Pageable pageable, BookFilter bookFilter) {
        Specification<BookEntity> specification = BookSpecification.create(bookFilter);
        Page<BookEntity> entities = bookRepository.findAll(specification, pageable);
        Page<Book> books = bookMapper.toPages(entities, new CyclePreventiveContext());

        for (Book book : books.getContent()) {
            self.cache(book);
        }

        return books;
    }

    @Transactional
    public Book createBook(Book book) {
        if (book.getId() != null)
            throw new MyRuntimeException("NOT CREATED", "Book to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        BookEntity entity = bookMapper.toEntity(book, new CyclePreventiveContext());
        entity = bookRepository.save(entity);
        return bookMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    @CachePut(key = "#book.id")
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
    @CacheEvict(key = "#bookId")
    public void deleteBookById(Long bookId) {
        Optional<BookEntity> optionalEntity = bookRepository.findById(bookId);

        if (!optionalEntity.isPresent())
            throw setExceptionWhenBookNotExist(bookId);

        bookRepository.deleteById(bookId);
    }

    @Transactional
    @Cacheable(key = "#bookId")
    public Book getBookById(Long bookId) {
        BookEntity bookEntity = bookRepository
                .findById(bookId)
                .orElseThrow(()-> setExceptionWhenBookNotExist(bookId));

        return bookMapper.toModel(bookEntity, new CyclePreventiveContext());
    }

    @CachePut(key = "#book.id")
    public Book cache(Book book) {
        return book;
    }

    private MyRuntimeException setExceptionWhenBookNotExist(Long bookId) {
        return new MyRuntimeException("NOT FOUND", "Book with id \"" + bookId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }

    public List<Book> getAll() {
        List<BookEntity> books = (List<BookEntity>) bookRepository.findAll();
        return bookMapper.toModels(books, new CyclePreventiveContext());
    }
}