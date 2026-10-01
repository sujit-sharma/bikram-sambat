package io.github.sujitsharma.bikramsambat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BsPeriodTest {

    @Test
    void betweenSameMonthAndDayAcrossYearsIsWholeYears() {
        BsPeriod period = BsPeriod.between(BsDate.of(2082, 1, 1), BsDate.of(2083, 1, 1));
        assertEquals(1, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
    }

    @Test
    void betweenSameDateIsZero() {
        BsDate date = BsDate.of(2082, 5, 10);
        assertTrue(BsPeriod.between(date, date).isZero());
    }

    @Test
    void toTotalMonthsCombinesYearsAndMonths() {
        BsPeriod period = BsPeriod.of(2, 3, 0);
        assertEquals(27, period.toTotalMonths());
    }

    @Test
    void isNegativeDetectsNegativeComponents() {
        assertFalse(BsPeriod.of(1, 0, 0).isNegative());
        assertTrue(BsPeriod.of(-1, 0, 0).isNegative());
    }

    @Test
    void toStringMirrorsIsoPeriodFormat() {
        assertEquals("P0D", BsPeriod.zero().toString());
        assertEquals("P1Y2M3D", BsPeriod.of(1, 2, 3).toString());
        assertEquals("P5D", BsPeriod.of(0, 0, 5).toString());
    }

    @Test
    void equalsAndHashCodeAreValueBased() {
        assertEquals(BsPeriod.of(1, 2, 3), BsPeriod.of(1, 2, 3));
        assertEquals(BsPeriod.of(1, 2, 3).hashCode(), BsPeriod.of(1, 2, 3).hashCode());
    }
}