package com.sahin.library_management.infra.model.pay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardTransaction extends Transaction {
    private Integer[] cardNumber;
    private Long expireDate;
    private Integer cvc;
}
