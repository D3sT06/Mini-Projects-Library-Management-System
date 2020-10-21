package com.sahin.library_management.infra.model.book;

import com.sahin.library_management.infra.model.account.Member;
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
    private BookItem bookItem;

    @NotNull
    private Member member;
    private Long loanedAt;
    private Long dueDate;
    private Long returnedAt;
}
