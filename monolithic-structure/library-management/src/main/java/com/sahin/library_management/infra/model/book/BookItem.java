package com.sahin.library_management.infra.model.book;

import com.sahin.library_management.infra.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookItem {

    private String barcode;

    @NotNull
    private Book book;

    @NotNull
    private Rack rack;

    private BookStatus status;
}