package com.sahin.library_management.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@ApiModel
@Getter
@Setter
public class AuthorCreateRequest {

    @ApiModelProperty(required = true, example = "George")
    private String name;

    @ApiModelProperty(required = true, example = "Orwell")
    private String surname;
}
