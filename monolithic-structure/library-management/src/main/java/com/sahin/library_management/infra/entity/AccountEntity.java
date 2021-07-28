package com.sahin.library_management.infra.entity;

import com.sahin.library_management.infra.enums.AccountFor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountEntity extends EntityWithUUID {

    private LibraryCardEntity libraryCard;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private AccountFor type;
}