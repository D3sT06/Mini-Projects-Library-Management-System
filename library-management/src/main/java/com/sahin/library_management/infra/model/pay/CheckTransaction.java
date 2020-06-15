package com.sahin.library_management.infra.model.pay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckTransaction extends Transaction {
    private String bankName;
    private Integer[] checkNumber;
}