package com.sahin.library_management.infra.model.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
    private Long id;
    private String title;
    private Long publicationDate;
    private BookCategory category;
    private Author author;
}