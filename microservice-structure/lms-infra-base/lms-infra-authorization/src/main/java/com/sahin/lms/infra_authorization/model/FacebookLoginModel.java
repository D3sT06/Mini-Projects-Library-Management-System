package com.sahin.lms.infra_authorization.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacebookLoginModel {

    @NotBlank
    private String accessToken;
}
