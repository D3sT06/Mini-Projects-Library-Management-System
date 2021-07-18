package com.sahin.lms.infra_model.library.model;

import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Setter
@ToString
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