package com.sahin.library_management.infra.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahin.library_management.infra.enums.LoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountLoginType {

    private Long id;

    @JsonIgnore
    private LibraryCard libraryCard;

    private LoginType type;
    private String typeSpecificKey;
}
