package com.sahin.library_management.infra.entity;

import java.util.Set;

public class BookCategoryEntity extends EntityWithUUID {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}