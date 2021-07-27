package com.sahin.library_management.infra.projections;

import javax.validation.constraints.NotNull;

public class AuthorProjections {

    public interface AuthorView {
        Long getId();

        @NotNull
        String getName();

        @NotNull
        String getSurname();
    }
}
