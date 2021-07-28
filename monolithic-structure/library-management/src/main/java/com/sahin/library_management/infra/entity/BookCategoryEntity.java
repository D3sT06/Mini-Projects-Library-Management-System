package com.sahin.library_management.infra.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookCategoryEntity extends EntityWithUUID {

    private String name;
}