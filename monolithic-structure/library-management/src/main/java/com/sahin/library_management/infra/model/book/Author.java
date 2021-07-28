package com.sahin.library_management.infra.model.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Author {

    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String surname;

    @JsonProperty(value = "fullname", access = JsonProperty.Access.READ_ONLY)
    public String fullName() {
        return getName() + " " + getSurname();
    }
}
