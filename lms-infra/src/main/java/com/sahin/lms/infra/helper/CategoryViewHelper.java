package com.sahin.lms.infra.helper;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import com.sahin.library_management.infra.projections.CategoryProjections;
import lombok.Getter;

import java.util.Set;

@Getter
public class CategoryViewHelper implements CategoryProjections.CategoryView {

    private Long id;
    private String name;
    private Set<BookEntity> books;
}
