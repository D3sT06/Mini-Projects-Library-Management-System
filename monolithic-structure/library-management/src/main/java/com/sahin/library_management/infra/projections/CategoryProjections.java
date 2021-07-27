package com.sahin.library_management.infra.projections;

import com.fasterxml.jackson.annotation.*;
import com.sahin.library_management.infra.entity.jpa.BookEntity;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class CategoryProjections {

    @JsonPropertyOrder({"id","name","bookIds"})
    public interface CategoryView {
        Long getId();

        @NotNull
        String getName();

        @NotNull
        @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id")
        @JsonIdentityReference(alwaysAsId = true)
        @JsonProperty("bookIds")
        Set<BookEntity> getBooks();
    }
}
