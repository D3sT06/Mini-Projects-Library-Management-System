package com.sahin.lms.infra_model.account;

import com.sahin.lms.infra_authorization.model.LibraryCard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public abstract class Account {
    private Long id;
    private LibraryCard libraryCard;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String email;
    private String phone;
}
