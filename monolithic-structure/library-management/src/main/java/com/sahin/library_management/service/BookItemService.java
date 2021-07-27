package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.BookItemEntity;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.mapper.BookItemMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.BookItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookItemService {

    @Autowired
    private BookItemRepository bookItemRepository;

    @Autowired
    private BookItemMapper bookItemMapper;

    public BookItem createBookItem(BookItem bookItem) {
        bookItem.setStatus(BookStatus.AVAILABLE);
        BookItemEntity entity = bookItemMapper.toEntity(bookItem, new CyclePreventiveContext());
        BookItemEntity createdEntity = bookItemRepository.save(entity);
        return bookItemMapper.toModel(createdEntity, new CyclePreventiveContext());
    }

    public BookItem updateBookItem(BookItem bookItem) {
        BookItemEntity entity = bookItemMapper.toEntity(bookItem, new CyclePreventiveContext());
        BookItemEntity updatedEntity = bookItemRepository.save(entity);
        return bookItemMapper.toModel(updatedEntity, new CyclePreventiveContext());
    }

    public void deleteBookItemByBarcode(String barcode) {
        bookItemRepository.deleteById(barcode);
    }

    public BookItem getBookItemByBarcode(String barcode) {
        BookItemEntity entity = bookItemRepository
                .findById(barcode)
                .get();

        return bookItemMapper.toModel(entity, new CyclePreventiveContext());
    }

    public List<BookItem> getBookItemByBookId(Long bookId) {
        List<BookItemEntity> entities = bookItemRepository
                .findByBookId(bookId);

        return bookItemMapper.toModels(entities, new CyclePreventiveContext());
    }
}