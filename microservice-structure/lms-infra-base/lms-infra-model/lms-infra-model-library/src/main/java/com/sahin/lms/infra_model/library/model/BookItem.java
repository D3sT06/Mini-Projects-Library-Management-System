package com.sahin.lms.infra_model.library.model;

import com.sahin.lms.infra_aop.annotation.NullableUUIDFormat;
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
}