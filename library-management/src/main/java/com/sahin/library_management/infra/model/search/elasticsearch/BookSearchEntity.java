package com.sahin.library_management.infra.model.search.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Getter
@Setter
@Document(indexName = "books")
public class BookSearchEntity {

    @Id
    private Long id;

    private String title;

    @Field(type = FieldType.Date, pattern = "dd/MM/yyyy")
    private LocalDate publicationDate;

    /*
    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<BookCategorySearchEntity> categories;

    @Field(type = FieldType.Nested, includeInParent = true)
    private AuthorSearchEntity author;*/
}
