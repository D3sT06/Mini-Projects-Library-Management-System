package com.sahin.library_management.swagger.model;

import com.sahin.library_management.infra.enums.BookStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class BookItemUpdateRequest extends BookItemCreateRequest {

    @ApiModelProperty(required = true, example = "1ed46d5d-b530-4f47-bb24-88d966b8a541")
    private String barcode;

    @ApiModelProperty(required = true)
    private BookStatus status;
}
