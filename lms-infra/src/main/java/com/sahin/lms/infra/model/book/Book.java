package com.sahin.lms.infra.model.book;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sahin.library_management.infra.serializer.LocalDateDeserializer;
import com.sahin.library_management.infra.serializer.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class Book {
    private Long id;

    @NotNull
    private String title;

    @Past
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate publicationDate;

    @NotNull
    private Set<BookCategory> categories;

    @NotNull
    private Author author;
}