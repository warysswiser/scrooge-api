package com.warys.scrooge.domain.common.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateAdjuster {

    public static LocalDate adjustedFrom(LocalDate from) {
        if (from == null) {
            return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        }
        return from;
    }

    public static LocalDate adjustedTo(LocalDate from, LocalDate to) {
        if(from == null)
            throw new IllegalArgumentException("from value must not be null");

        if (to == null || to.isBefore(from)) {
            return from.plusMonths(1);
        }
        return to;
    }
}
