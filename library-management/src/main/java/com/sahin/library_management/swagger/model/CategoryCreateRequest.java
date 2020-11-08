package com.sahin.library_management.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class CategoryCreateRequest {

    @ApiModelProperty(required = true, example = "Edebiyat")
    private String name;
}
