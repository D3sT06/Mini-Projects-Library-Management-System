package com.sahin.library_management.infra.model.search;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.entity.BookCategoryEntity;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Setter
public class BookFilter {

    private String title;
    private List<Long> authorIds;
    private List<Long> categoryIds;

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }
    public Optional<List<Long>> getAuthorIds() {
        return Optional.ofNullable(authorIds);
    }
    public Optional<List<Long>> getCategoryIds() {
        return Optional.ofNullable(categoryIds);
    }
}
