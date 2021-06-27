package com.sahin.library_management.infra.model.account;

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
