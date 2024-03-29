package com.sahin.lms.library_service.projection;

import com.fasterxml.jackson.annotation.*;
import com.sahin.lms.infra_entity.library.jpa.BookEntity;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AuthorProjections {

    @JsonPropertyOrder({"id","fullname","bookIds"})
    public interface AuthorView {
        Long getId();

        @NotNull
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String getName();

        @NotNull
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String getSurname();

        @JsonProperty(value = "fullname", access = JsonProperty.Access.READ_ONLY)
        @Value("#{target.name + ' ' + target.surname}")
        String getFullname();

        @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id")
        @JsonIdentityReference(alwaysAsId = true)
        @JsonProperty("bookIds")
        @ApiModelProperty(dataType = "[Ljava.lang.Long;")
        List<BookEntity> getBooks();
    }
}
