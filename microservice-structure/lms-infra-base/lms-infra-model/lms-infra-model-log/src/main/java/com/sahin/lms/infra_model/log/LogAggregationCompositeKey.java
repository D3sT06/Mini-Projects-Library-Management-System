package com.sahin.lms.infra_model.log;

import com.sahin.lms.infra_enum.LogAction;
import com.sahin.lms.infra_enum.QueryTerm;
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
