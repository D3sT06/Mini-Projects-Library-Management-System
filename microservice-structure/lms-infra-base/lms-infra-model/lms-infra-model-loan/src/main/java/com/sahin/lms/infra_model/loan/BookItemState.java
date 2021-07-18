package com.sahin.lms.infra_model.loan;

import com.sahin.lms.infra_aop.annotation.NullableUUIDFormat;
import com.sahin.lms.infra_enum.BookStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookItemState {

    @NullableUUIDFormat
    private String barcode;

    @NotNull
    private BookStatus status;

}
