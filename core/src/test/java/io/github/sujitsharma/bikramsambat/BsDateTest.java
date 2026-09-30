package io.github.sujitsharma.bikramsambat;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BsDateTest {

    @Test
    void ofCreatesDateAndToStringIsIsoLike() {
        BsDate date = BsDate.of(2082, 6, 13);
        assertEquals("2082-06-13", date.toString());
        assertEquals(2082, date.getYear());
        assertEquals(6, date.getMonthValue());
        assertEquals(13, date.getDayOfMonth());
    }

    @Test
    void ofRejectsInvalidMonth() {
        assertThrows(BsDateException.class, () -> BsDate.of(2082, 13, 1));
        assertThrows(BsDateException.class, () -> BsDate.of(2082, 0, 1));
    }

    @Test
    void ofRejectsInvalidDay() {
        // BS 2082 month 8 (Mangsir) has 29 days per the calendar table.
        assertThrows(BsDateException.class, () -> BsDate.of(2082, 8, 30));
        assertThrows(BsDateException.class, () -> BsDate.of(2082, 1, 0));
    }

    @Test
    void ofRejectsYearOutOfSupportedRange() {
        assertThrows(BsDateException.class, () -> BsDate.of(1969, 1, 1));
        assertThrows(BsDateException.class, () -> BsDate.of(2101, 1, 1));
    }

    @Test
    void roundTripsThroughLocalDate() {
        for (BsDate original : new BsDate[] {
                BsDate.of(1982, 1, 1),
                BsDate.of(2000, 6, 15),
                BsDate.of(2049, 12, 30),
                BsDate.of(2082, 9, 5),
                BsDate.of(2100, 12, BsDate.of(2100, 12, 1).lengthOfMonth()),
        }) {
            LocalDate ad = original.toLocalDate();
            BsDate roundTripped = BsDate.from(ad);
            assertEquals(original, roundTripped, "round-trip failed for " + original);
        }
    }

    @Test
    void plusDaysCrossesMonthAndYearBoundaries() {
        BsDate lastDayOfYear = BsDate.of(2082, 12, BsDate.of(2082, 12, 1).lengthOfMonth());
        BsDate next = lastDayOfYear.plusDays(1);
        assertEquals(BsDate.of(2083, 1, 1), next);
    }

    @Test
    void plusMonthsClampsDayToShorterMonth() {
        // BS 2082 month 7 (Kartik) has 30 days, month 8 (Mangsir) has 29.
        BsDate date = BsDate.of(2082, 7, 30);
        BsDate result = date.plusMonths(1);
        assertEquals(BsDate.of(2082, 8, 29), result);
    }

    @Test
    void plusYearsKeepsMonthAndDayWhenValid() {
        BsDate date = BsDate.of(2082, 1, 1);
        assertEquals(BsDate.of(2083, 1, 1), date.plusYears(1));
    }

    @Test
    void comparisonsAndOrdering() {
        BsDate a = BsDate.of(2082, 1, 1);
        BsDate b = BsDate.of(2082, 1, 2);
        assertTrue(a.isBefore(b));
        assertTrue(b.isAfter(a));
        assertFalse(a.isEqual(b));
        assertTrue(a.isEqual(BsDate.of(2082, 1, 1)));
    }

    @Test
    void untilDaysMatchesEpochDayDifference() {
        BsDate start = BsDate.of(2082, 1, 1);
        BsDate end = BsDate.of(2082, 2, 1);
        long days = start.until(end, ChronoUnit.DAYS);
        assertEquals(start.lengthOfMonth(), days);
    }

    @Test
    void parseAndFormatRoundTrip() {
        BsDate date = BsDate.of(2082, 6, 13);
        assertEquals(date, BsDate.parse("2082-06-13"));
        assertEquals("2082-06-13", date.format("yyyy-MM-dd"));
    }
}