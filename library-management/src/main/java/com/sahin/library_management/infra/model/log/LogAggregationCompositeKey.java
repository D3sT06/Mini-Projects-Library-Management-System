package com.sahin.library_management.infra.model.log;

import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.QueryTerm;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LogAggregationCompositeKey implements Serializable {
    private String barcode;
    private LogAction action;
    private QueryTerm queryTerm;
}
