package com.sahin.lms.library_service.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class BookItemCreateRequest {

    @ApiModelProperty(required = true)
    private ModelWithOnlyId book;

    @ApiModelProperty(required = true)
    private ModelWithOnlyId rack;
}
