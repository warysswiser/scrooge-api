package com.warys.scrooge.domain.common.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DateAdjusterShould {

    public static final LocalDate FROM = LocalDate.of(2020, 8, 12);

    @Test
    void not_adjust_date_when_it_is_already_valid() {
        final LocalDate to = LocalDate.of(2020, 10, 12);

        final LocalDate adjustedFrom = DateAdjuster.adjustedFrom(FROM);
        final LocalDate adjustedTo = DateAdjuster.adjustedTo(FROM, to);

        assertThat(adjustedFrom).isSameAs(FROM);
        assertThat(adjustedTo).isSameAs(to);
    }

    @Test
    void adjust_from_when_it_is_null(){
        final LocalDate adjustedFrom = DateAdjuster.adjustedFrom(null);

        assertThat(adjustedFrom).isNotNull();
    }

    @Test
    void adjust_to_when_from_is_valid_and_to_is_null() {
        final LocalDate adjustedFrom = DateAdjuster.adjustedFrom(FROM);
        final LocalDate adjustedTo = DateAdjuster.adjustedTo(FROM, null);

        assertThat(adjustedFrom).isSameAs(FROM);
        assertThat(adjustedTo).isEqualTo(LocalDate.of(2020, 9, 12));
    }

    @Test
    void adjust_to_when_from_is_after_to() {
        final LocalDate to = LocalDate.of(2020, 8, 2);

        final LocalDate adjustedTo = DateAdjuster.adjustedTo(FROM, to);

        assertThat(adjustedTo).isEqualTo(LocalDate.of(2020, 9, 12));
    }

    @Test
    void throw_IllegalArgumentException_when_adjustedTo_with_null_from_value() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DateAdjuster.adjustedTo(null, LocalDate.of(2020, 8, 12)));
    }
}