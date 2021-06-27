package com.sahin.lms.infra.model.log;

import com.sahin.lms.infra.enums.AccountFor;
import com.sahin.lms.infra.enums.LogAction;
import com.sahin.lms.infra.enums.QueryTerm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAggregation {

    private String barcode;
    protected long createdDate;
    protected long lastModifiedDate;
    private LogAction action;
    private Long actionCount;
    private AccountFor accountFor;
    private QueryTerm queryTerm;
}
