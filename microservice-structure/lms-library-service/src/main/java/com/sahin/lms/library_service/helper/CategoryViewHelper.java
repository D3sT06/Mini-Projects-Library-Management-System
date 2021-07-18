package com.sahin.lms.library_service.helper;

import com.sahin.lms.infra_entity.library.jpa.BookEntity;
import com.sahin.lms.library_service.projection.CategoryProjections;
import lombok.Getter;

import java.util.Set;

@Getter
public class CategoryViewHelper implements CategoryProjections.CategoryView {

    private Long id;
    private String name;
    private Set<BookEntity> books;
}
