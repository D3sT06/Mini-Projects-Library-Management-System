package com.sahin.lms.infra_model.library.model;

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

    @JsonProperty(value = "fullname", access = JsonProperty.Access.READ_ONLY)
    public String fullName() {
        return getName() + " " + getSurname();
    }
}
