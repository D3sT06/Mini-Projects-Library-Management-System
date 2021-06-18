package com.sahin.lms.library_service.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel
@Getter
@Setter
public class BookFilterRequest {

    @ApiModelProperty(example = "hay")
    private String title;

    @ApiModelProperty(example = "[1]")
    private List<Long> authorIds;

    @ApiModelProperty(example = "[1]")
    private List<Long> categoryIds;
}
