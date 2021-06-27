package com.sahin.library_management.infra.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {
    private String barcode;
    private String password;
}

