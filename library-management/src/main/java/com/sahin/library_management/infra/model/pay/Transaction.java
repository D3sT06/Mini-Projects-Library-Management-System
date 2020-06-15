package com.sahin.library_management.infra.model.pay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private Long createdAt;
    private Double amount;
}
