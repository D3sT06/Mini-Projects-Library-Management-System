package com.sahin.library_management.infra.model.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Author {

    private Long id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String surname;

    @JsonProperty(value = "full name", access = JsonProperty.Access.READ_ONLY)
    public String fullName() {
        return getName() + " " + getSurname();
    }
}
