package com.sahin.library_management.infra.entity;

import com.sahin.library_management.infra.enums.AccountFor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LibraryCardEntity extends EntityWithUUID {

    private Long issuedAt;
    private String password;
    private Boolean active;
    private AccountFor accountFor;
    private Set<AccountLoginTypeEntity> loginTypes;
}