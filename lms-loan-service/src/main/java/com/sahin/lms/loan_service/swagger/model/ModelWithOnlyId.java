package com.sahin.lms.loan_service.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class ModelWithOnlyId {

    @ApiModelProperty(required = true, example = "1")
    private Long id;
}
