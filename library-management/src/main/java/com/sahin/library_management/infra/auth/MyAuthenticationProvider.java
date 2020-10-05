package com.sahin.library_management.infra.auth;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    public MyAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.setPasswordEncoder(passwordEncoder);
    }
}
