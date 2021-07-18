package com.sahin.lms.infra_model.library.model;

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
