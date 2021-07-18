package com.sahin.lms.infra_model.loan;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class BookReserving {
    private Long id;

    @NotNull
    private BookItemState bookItemState;

    @NotNull
    private Long memberId;
    private Long reservedAt;
    private Long dueDate;
    private Long expectedLoanAt;
}
