package com.sahin.library_management.infra.model.search;

import com.sahin.library_management.infra.entity_model.AuthorEntity;
import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Setter
public class BookFilter {

    private String title;
    private List<AuthorEntity> authors;
    private List<BookCategoryEntity> categories;

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }
    public Optional<List<AuthorEntity>> getAuthors() {
        return Optional.ofNullable(authors);
    }
    public Optional<List<BookCategoryEntity>> getCategories() {
        return Optional.ofNullable(categories);
    }
}
