package com.sahin.lms.loan_service.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class BookReservationUpdateRequest {

    @ApiModelProperty(required = true, example = "1")
    private Long id;

    @ApiModelProperty(required = true)
    private ModelWithOnlyId bookItem;

    @ApiModelProperty(required = true)
    private ModelWithOnlyId member;

    @ApiModelProperty(example = "1601740091000")
    private Long reservedAt;

    @ApiModelProperty(example = "1608540036000")
    private Long dueDate;

    @ApiModelProperty(example = "1604210041000")
    private Long expectedLoanAt;
}