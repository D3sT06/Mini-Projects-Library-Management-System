package com.sahin.lms.infra.helper;

import com.sahin.lms.infra.entity.jpa.BookEntity;
import com.sahin.lms.infra.projections.CategoryProjections;
import lombok.Getter;

import java.util.Set;

@Getter
public class CategoryViewHelper implements CategoryProjections.CategoryView {

    private Long id;
    private String name;
    private Set<BookEntity> books;
}
