package com.sahin.library_management.infra.model.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Account {
    private Long id;
    private LibraryCard libraryCard;
    private String name;
    private String surname;
    private String email;
    private String phone;
}
