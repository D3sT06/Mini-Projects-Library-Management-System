package com.sahin.library_management.infra.model.book;

import com.sahin.library_management.infra.model.account.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookLoaning {
    private Long id;
    private BookItem bookItem;
    private Member member;
    private Long loanedAt;
    private Long dueDate;
    private Long returnedAt;
}
