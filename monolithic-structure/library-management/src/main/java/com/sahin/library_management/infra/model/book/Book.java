package com.sahin.library_management.infra.model.book;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sahin.library_management.infra.serializer.LocalDateDeserializer;
import com.sahin.library_management.infra.serializer.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class Book {
    private Long id;
    private String title;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate publicationDate;

    private Set<BookCategory> categories;
    private Author author;
}