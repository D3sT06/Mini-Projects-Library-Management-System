package com.sahin.library_management.infra.model.book;

import com.sahin.library_management.infra.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class BookItem {
    private String barcode;
    private Book book;
    private Rack rack;
    private BookStatus status;
}