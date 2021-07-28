package com.sahin.library_management.infra.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class BookEntity extends EntityWithUUID {

    private String title;
    private LocalDate publicationDate;
    private Set<BookCategoryEntity> categories;
    private AuthorEntity author;
}
