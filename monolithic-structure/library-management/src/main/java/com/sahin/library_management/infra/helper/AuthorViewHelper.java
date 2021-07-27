package com.sahin.library_management.infra.helper;

import com.sahin.library_management.infra.projections.AuthorProjections;
import lombok.Getter;

@Getter
public class AuthorViewHelper implements AuthorProjections.AuthorView {

    private Long id;
    private String name;
    private String surname;
}
