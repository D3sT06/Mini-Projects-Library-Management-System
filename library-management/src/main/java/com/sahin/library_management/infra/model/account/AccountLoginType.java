package com.sahin.library_management.infra.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahin.library_management.infra.enums.LoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class AccountLoginType implements Serializable {

    private static final long serialVersionUID = 5112345688877468931L;

    private Long id;

    @JsonIgnore
    private LibraryCard libraryCard;

    private LoginType type;
    private String typeSpecificKey;
}
