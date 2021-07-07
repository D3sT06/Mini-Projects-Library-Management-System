package com.sahin.lms.infraauth.auth;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    public MyAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.setPasswordEncoder(passwordEncoder);
    }
}
