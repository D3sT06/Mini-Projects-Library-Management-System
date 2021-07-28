package com.sahin.library_management.infra.model.book;

import com.sahin.library_management.infra.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookItem {

    private String barcode;
    private Book book;
    private Rack rack;
    private BookStatus status;
}