package com.sahin.lms.infra.model.book;

import com.sahin.lms.infra.annotation.NullableUUIDFormat;
import com.sahin.lms.infra.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookItem {

    @NullableUUIDFormat
    private String barcode;

    @NotNull
    private Book book;

    @NotNull
    private Rack rack;

    private BookStatus status;
}