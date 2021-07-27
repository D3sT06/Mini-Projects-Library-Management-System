package com.sahin.library_management.infra.helper;

import com.sahin.library_management.infra.projections.CategoryProjections;
import lombok.Getter;

@Getter
public class CategoryViewHelper implements CategoryProjections.CategoryView {

    private Long id;
    private String name;
}
