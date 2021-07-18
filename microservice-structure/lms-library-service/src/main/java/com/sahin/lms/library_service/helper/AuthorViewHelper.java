package com.sahin.lms.library_service.helper;

import com.sahin.lms.infra_entity.library.jpa.BookEntity;
import com.sahin.lms.library_service.projection.AuthorProjections;
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
