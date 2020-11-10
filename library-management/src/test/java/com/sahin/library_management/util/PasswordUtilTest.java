package com.sahin.library_management.util;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Password Utility Tests")
class PasswordUtilTest {

    @RepeatedTest(10)
    @DisplayName("Random password can be created")
    void createRandomPassword() {
        String password = PasswordUtil.createRandomPassword();

        assertAll(
                () -> assertNotNull(password, "Password is null"),
                () -> assertEquals(6, password.length(), "Password length is false"),
                () -> assertTrue(password.matches("^[a-zA-Z0-9]*$"), "Password is not completely alphanumeric.")
        );
    }
}
