package com.sahin.library_management.infra.model.book;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookCategory {

    private Long id;

    @NotNull
    private String name;
}
