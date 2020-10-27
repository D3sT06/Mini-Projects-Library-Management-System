package com.sahin.library_management.infra.model.book;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class BookCategory {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @JsonBackReference
    private Set<Book> books;
}
