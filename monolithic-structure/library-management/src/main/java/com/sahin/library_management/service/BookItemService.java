package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.BookItemEntity;
import com.sahin.library_management.repository.jpa.BookItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookItemService {

    @Autowired
    private BookItemRepository bookItemRepository;

    public BookItemEntity createBookItem(BookItemEntity bookItem) {
        return bookItemRepository.save(bookItem);
    }

    public BookItemEntity updateBookItem(BookItemEntity bookItem) {
        return bookItemRepository.save(bookItem);
    }

    public void deleteBookItemByBarcode(String barcode) {
        bookItemRepository.deleteById(barcode);
    }

    public BookItemEntity getBookItemByBarcode(String barcode) {
        return bookItemRepository
                .findById(barcode)
                .get();
    }

    public List<BookItemEntity> getBookItemByBookId(Long bookId) {
        return bookItemRepository
                .findByBookId(bookId);
    }
}