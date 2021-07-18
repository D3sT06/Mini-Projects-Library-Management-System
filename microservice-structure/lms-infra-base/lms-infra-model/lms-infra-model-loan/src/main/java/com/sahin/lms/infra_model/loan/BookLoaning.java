package com.sahin.lms.infra_model.loan;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookLoaning {

    private Long id;

    @NotNull
    private BookItemState bookItemState;

    @NotNull
    private Long memberId;
    private Long loanedAt;
    private Long dueDate;
    private Long returnedAt;
}
