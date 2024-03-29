package com.sahin.library_management.factory;

import com.sahin.library_management.infra.enums.TimeUnit;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.EnumMap;

@Component
public class ChronoUnitFactory {

    private EnumMap<TimeUnit, ChronoUnit> timeChronoMap;

    public ChronoUnitFactory() {
        timeChronoMap = new EnumMap<>(TimeUnit.class);
        timeChronoMap.put(TimeUnit.SECONDS, ChronoUnit.SECONDS);
        timeChronoMap.put(TimeUnit.MINUTES, ChronoUnit.SECONDS);
        timeChronoMap.put(TimeUnit.HOURS, ChronoUnit.HOURS);
        timeChronoMap.put(TimeUnit.DAYS, ChronoUnit.DAYS);
    }

    public ChronoUnit get(TimeUnit timeUnit) {
        return timeChronoMap.get(timeUnit);
    }
}
