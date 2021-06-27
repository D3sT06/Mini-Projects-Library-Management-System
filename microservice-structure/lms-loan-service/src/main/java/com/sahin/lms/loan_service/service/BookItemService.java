package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra.entity.jpa.BookItemEntity;
import com.sahin.lms.infra.enums.BookStatus;
import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.loan_service.repository.BookItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookItemService {

    @Autowired
    private BookItemRepository bookItemRepository;

    public void updateStatus(String bookItemBarcode, BookStatus newStatus) {
        Optional<BookItemEntity> oldEntity = bookItemRepository.findById(bookItemBarcode);
        if (!oldEntity.isPresent())
            throw new MyRuntimeException("NOT FOUND", "Book item with barcode \"" + bookItemBarcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        oldEntity.get().setStatus(newStatus);
        bookItemRepository.save(oldEntity.get());
    }
}
