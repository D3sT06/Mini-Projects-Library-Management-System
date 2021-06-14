package com.sahin.lms.infra.helper;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import com.sahin.library_management.infra.projections.AuthorProjections;
import lombok.Getter;

import java.util.List;

@Getter
public class AuthorViewHelper implements AuthorProjections.AuthorView {

    private Long id;
    private String name;
    private String surname;
    private String fullname;
    private List<BookEntity> books;
}
