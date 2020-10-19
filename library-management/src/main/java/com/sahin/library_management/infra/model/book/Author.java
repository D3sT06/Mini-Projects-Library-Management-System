package com.sahin.library_management.infra.model.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Author {

    private Long id;

    @JsonIgnore
    @NotNull
    private String name;

    @JsonIgnore
    @NotNull
    private String surname;

    @JsonProperty("full name")
    public String fullName() {
        return getName() + " " + getSurname();
    }
}
