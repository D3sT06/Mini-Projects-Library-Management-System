package com.sahin.library_management.infra.projections;

import javax.validation.constraints.NotNull;

public class CategoryProjections {

    public interface CategoryView {
        Long getId();

        @NotNull
        String getName();
    }
}
