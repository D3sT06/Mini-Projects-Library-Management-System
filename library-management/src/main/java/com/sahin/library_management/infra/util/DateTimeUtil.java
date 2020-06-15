package com.sahin.library_management.infra.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeUtil {

    private DateTimeUtil() {}

    public static LocalDateTime toLocalDateTime(Long epochInMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochInMilli), ZoneOffset.UTC);
    }

}
