package com.sahin.library_management.swagger.controller;

import com.sahin.library_management.infra.model.auth.LoginModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Authentication")
@RequestMapping(value = "/api/auth")
@RestController
public class AuthenticationSwaggerApi {

    @ApiOperation(value = "Login with username and password",
            notes = "Use a1111111-1111-1111-1111-111111111111 for librarian barcode and\n" +
                    "a1111111-1111-1111-1111-11111111114 for member barcode.\n" +
                    "Password is 1234 for both!")
    @PostMapping("/login")
    public void login(@RequestBody LoginModel loginModel) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}
