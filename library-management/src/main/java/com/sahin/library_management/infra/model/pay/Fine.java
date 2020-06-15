package com.sahin.library_management.infra.model.pay;

import com.sahin.library_management.infra.model.book.BookLoaning;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Fine {
    private Long id;
    private BookLoaning bookLoaning;
    private double amount;
    private boolean paid;
}
