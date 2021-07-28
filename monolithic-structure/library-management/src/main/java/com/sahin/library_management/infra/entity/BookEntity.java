package com.sahin.library_management.infra.entity;

import java.time.LocalDate;
import java.util.Set;

public class BookEntity extends EntityWithUUID {

    private String title;
    private LocalDate publicationDate;
    private Set<BookCategoryEntity> categories;
    private AuthorEntity author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Set<BookCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<BookCategoryEntity> categories) {
        this.categories = categories;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }
}
