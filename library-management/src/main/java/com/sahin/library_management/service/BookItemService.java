package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity_model.BookItemEntity;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.mapper.BookItemMapper;
import com.sahin.library_management.repository.BookItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookItemService {

    @Autowired
    private BookItemRepository bookItemRepository;

    @Autowired
    private BookItemMapper bookItemMapper;

    public void createBookItem(BookItem bookItem) {
        if (bookItem.getBarcode() != null || bookItem.getStatus() != null)
            throw new MyRuntimeException("Book item to be created cannot have a barcode or a state.", HttpStatus.BAD_REQUEST);

        bookItem.setStatus(BookStatus.AVAILABLE);
        BookItemEntity entity = bookItemMapper.toEntity(bookItem);
        bookItemRepository.save(entity);
    }

    public void updateBookItem(BookItem bookItem) {
        if (bookItem.getBarcode() == null)
            throw new MyRuntimeException("Book item to be created must have a barcode.", HttpStatus.BAD_REQUEST);

        Optional<BookItemEntity> oldEntity = bookItemRepository.findById(bookItem.getBarcode());
        if (!oldEntity.isPresent())
            throw new MyRuntimeException("Book item with barcode \"" + bookItem.getBarcode() + "\" not exist!", HttpStatus.BAD_REQUEST);

        BookItemEntity entity = bookItemMapper.toEntity(bookItem);
        bookItemRepository.save(entity);
    }

    public void deleteBookItemByBarcode(String barcode) {
        Optional<BookItemEntity> optionalEntity = bookItemRepository.findById(barcode);

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Book item with barcode \"" + barcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        bookItemRepository.deleteById(barcode);
    }

    public BookItem getBookItemByBarcode(String barcode) {
        BookItemEntity entity = bookItemRepository
                .findById(barcode)
                .orElseThrow(()-> new MyRuntimeException("Book item with barcode " + barcode + " not exist!", HttpStatus.BAD_REQUEST));

        return bookItemMapper.toModel(entity);
    }
}