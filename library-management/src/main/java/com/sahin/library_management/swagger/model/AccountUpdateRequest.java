package com.sahin.library_management.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class AccountUpdateRequest extends AccountCreateRequest {

    @ApiModelProperty(required = true, example = "1")
    private Long id;
}
