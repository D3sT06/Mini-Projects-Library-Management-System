package com.sahin.library_management.infra.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sahin.library_management.infra.enums.AccountFor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LibraryCard {

    private String barcode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Long issuedAt;
    private Boolean active;
    private AccountFor accountFor;
    private Set<AccountLoginType> loginTypes;
}
