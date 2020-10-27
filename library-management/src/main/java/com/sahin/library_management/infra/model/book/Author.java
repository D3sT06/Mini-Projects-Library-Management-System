package com.sahin.library_management.infra.model.book;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Author.class)
public class Author {

    private Long id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String surname;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("bookIds")
    private List<Book> books;

    @JsonProperty(value = "fullname", access = JsonProperty.Access.READ_ONLY)
    public String fullName() {
        return getName() + " " + getSurname();
    }
}
