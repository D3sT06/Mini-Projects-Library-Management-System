package com.sahin.library_management.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class AccountCreateRequest {

    @ApiModelProperty(required = true, example = "Serkan")
    private String name;

    @ApiModelProperty(required = true, example = "Sahin")
    private String surname;

    @ApiModelProperty(example = "serkan@sahin.com")
    private String email;

    @ApiModelProperty(example = "555-111-33")
    private String phone;
}
