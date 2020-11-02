package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.BookItemEntity;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.mapper.BookItemMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.BookItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
@CacheConfig(cacheNames = "bookItems")
public class BookItemService {

    @Autowired
    private BookItemRepository bookItemRepository;

    @Autowired
    private BookItemMapper bookItemMapper;

    @Resource
    private BookItemService self;

    public BookItem createBookItem(BookItem bookItem) {
        if (bookItem.getBarcode() != null || bookItem.getStatus() != null)
            throw new MyRuntimeException("NOT CREATED", "Book item to be created cannot have a barcode or a state.", HttpStatus.BAD_REQUEST);

        bookItem.setStatus(BookStatus.AVAILABLE);
        BookItemEntity entity = bookItemMapper.toEntity(bookItem, new CyclePreventiveContext());
        BookItemEntity createdEntity = bookItemRepository.save(entity);
        return bookItemMapper.toModel(createdEntity, new CyclePreventiveContext());
    }

    @CachePut(key = "#bookItem.barcode")
    public BookItem updateBookItem(BookItem bookItem) {
        if (bookItem.getBarcode() == null)
            throw new MyRuntimeException("NOT UPDATED", "Book item to be created must have a barcode.", HttpStatus.BAD_REQUEST);

        Optional<BookItemEntity> oldEntity = bookItemRepository.findById(bookItem.getBarcode());
        if (!oldEntity.isPresent())
            throw new MyRuntimeException("NOT FOUND", "Book item with barcode \"" + bookItem.getBarcode() + "\" not exist!", HttpStatus.BAD_REQUEST);

        BookItemEntity entity = bookItemMapper.toEntity(bookItem, new CyclePreventiveContext());
        BookItemEntity updatedEntity = bookItemRepository.save(entity);
        return bookItemMapper.toModel(updatedEntity, new CyclePreventiveContext());
    }

    @CacheEvict(key = "#barcode")
    public void deleteBookItemByBarcode(String barcode) {
        Optional<BookItemEntity> optionalEntity = bookItemRepository.findById(barcode);

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("NOT FOUND", "Book item with barcode \"" + barcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        bookItemRepository.deleteById(barcode);
    }

    @Cacheable(key = "#barcode")
    public BookItem getBookItemByBarcode(String barcode) {
        BookItemEntity entity = bookItemRepository
                .findById(barcode)
                .orElseThrow(()-> new MyRuntimeException("NOT FOUND", "Book item with barcode " + barcode + " not exist!", HttpStatus.BAD_REQUEST));

        return bookItemMapper.toModel(entity, new CyclePreventiveContext());
    }

    public List<BookItem> getBookItemByBookId(Long bookId) {
        List<BookItemEntity> entities = bookItemRepository
                .findByBookId(bookId);

        List<BookItem> bookItems = bookItemMapper.toModels(entities, new CyclePreventiveContext());
        for (BookItem item : bookItems)
            self.cache(item);

        return bookItems;
    }

    @CachePut(key = "#bookItem.barcode")
    public BookItem cache(BookItem bookItem) {
        return bookItem;
    }
}