package com.sahin.library_management.infra.entity;

import com.sahin.library_management.infra.enums.LoginType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountLoginTypeEntity extends EntityWithUUID {

    private LibraryCardEntity libraryCard;
    private LoginType type;
    private String typeSpecificKey;
}