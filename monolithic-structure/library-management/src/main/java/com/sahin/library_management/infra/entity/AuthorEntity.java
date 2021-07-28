package com.sahin.library_management.infra.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorEntity extends EntityWithUUID {

    private String name;
    private String surname;
    private List<BookEntity> books;
}
