package com.sahin.lms.infra.model.book;

import com.sahin.lms.infra.model.account.Member;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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