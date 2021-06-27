package com.sahin.library_management.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Date Time Utility Tests")
public class DateTimeUtilTest {

    @Test
    @DisplayName("Milliseconds can be converted into local date time")
    void toLocalDateTime() {
        Long epochInMilli = 1600000000000L;
        LocalDateTime localDateTime = DateTimeUtil.toLocalDateTime(epochInMilli);

        assertAll(
                () -> assertNotNull(localDateTime, "Local date time is null"),
                () -> assertEquals(2020, localDateTime.getYear(), "Year is incorrect"),
                () -> assertEquals(9, localDateTime.getMonthValue(), "Month is incorrect"),
                () -> assertEquals(13, localDateTime.getDayOfMonth(), "Day is incorrect"),
                () -> assertEquals(12, localDateTime.getHour(), "Hour is incorrect"),
                () -> assertEquals(26, localDateTime.getMinute(), "Minute is incorrect"),
                () -> assertEquals(40, localDateTime.getSecond(), "Second is incorrect")
        );
    }
}
