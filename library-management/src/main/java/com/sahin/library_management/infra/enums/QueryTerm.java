package com.sahin.library_management.infra.enums;

import lombok.Getter;

@Getter
public enum QueryTerm {

    DAILY(TimeUnit.DAYS, 1L),
    WEEKLY(TimeUnit.DAYS, 7L),
    HOURLY(TimeUnit.HOURS, 1L);

    private TimeUnit timeUnit;
    private Long amountToSubtract;

    QueryTerm(TimeUnit timeUnit, Long amountToSubtract) {
        this.timeUnit = timeUnit;
        this.amountToSubtract = amountToSubtract;
    }
}
