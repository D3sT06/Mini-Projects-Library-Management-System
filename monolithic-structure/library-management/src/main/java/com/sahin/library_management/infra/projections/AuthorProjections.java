package com.sahin.library_management.infra.projections;

import com.fasterxml.jackson.annotation.*;
import com.sahin.library_management.infra.entity.jpa.BookEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AuthorProjections {

    @JsonPropertyOrder({"id", "name", "surname", "bookIds"})
    public interface AuthorView {
        Long getId();

        @NotNull
        String getName();

        @NotNull
        String getSurname();

        @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id")
        @JsonIdentityReference(alwaysAsId = true)
        @JsonProperty("bookIds")
        List<BookEntity> getBooks();
    }
}
