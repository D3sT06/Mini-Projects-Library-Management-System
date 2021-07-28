package com.sahin.library_management.infra.entity;

import java.util.Set;

public class BookCategoryEntity extends EntityWithUUID {

    private String name;
    private Set<BookEntity> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BookEntity> getBooks() {
        return books;
    }

    public void setBooks(Set<BookEntity> books) {
        this.books = books;
    }
}