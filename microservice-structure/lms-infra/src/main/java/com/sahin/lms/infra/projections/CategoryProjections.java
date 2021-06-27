package com.sahin.lms.infra.projections;

import com.fasterxml.jackson.annotation.*;
import com.sahin.lms.infra.entity.jpa.BookEntity;
import io.swagger.annotations.ApiModelProperty;

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
        @ApiModelProperty(dataType = "[Ljava.lang.Long;")
        Set<BookEntity> getBooks();
    }
}
