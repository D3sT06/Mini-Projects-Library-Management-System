package com.sahin.lms.library_service.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@ApiModel
@Getter
@Setter
public class BookCreateRequest {

    @ApiModelProperty(required = true, example = "Kar")
    private String title;

    @ApiModelProperty(example = "12/10/2018")
    private String publicationDate;

    @ApiModelProperty(required = true)
    private Set<ModelWithOnlyId> categories;

    @ApiModelProperty(required = true)
    private ModelWithOnlyId author;
}
