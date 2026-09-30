package io.github.sujitsharma.bikramsambat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Converts between BS calendar dates and their epoch-day offset, and between
 * BS dates and {@link LocalDate}, using {@link BsCalendarData} as the source
 * of month lengths.
 */
final class BsEpochConverter {

    // BS MIN_YEAR-01-01 corresponds to this Gregorian date.
    private static final LocalDate AD_EPOCH = LocalDate.of(1913, 4, 13);

    // yearStartEpochDay[i] = number of days between AD_EPOCH and the first day
    // of BS year (MIN_YEAR + i). One extra trailing entry marks the day right
    // after the last supported year, for range checks.
    private static final long[] YEAR_START_EPOCH_DAY;

    static {
        int yearCount = BsCalendarData.MAX_YEAR - BsCalendarData.MIN_YEAR + 1;
        YEAR_START_EPOCH_DAY = new long[yearCount + 1];
        long acc = 0;
        for (int year = BsCalendarData.MIN_YEAR; year <= BsCalendarData.MAX_YEAR; year++) {
            YEAR_START_EPOCH_DAY[year - BsCalendarData.MIN_YEAR] = acc;
            acc += BsCalendarData.lengthOfYear(year);
        }
        YEAR_START_EPOCH_DAY[yearCount] = acc;
    }

    private BsEpochConverter() {
    }

    static long toEpochDay(int year, int month, int day) {
        BsCalendarData.validateYear(year);
        long days = YEAR_START_EPOCH_DAY[year - BsCalendarData.MIN_YEAR];
        int[] monthLengths = BsCalendarData.monthLengths(year);
        for (int m = 1; m < month; m++) {
            days += monthLengths[m - 1];
        }
        return days + (day - 1);
    }

    static LocalDate toLocalDate(int year, int month, int day) {
        return AD_EPOCH.plusDays(toEpochDay(year, month, day));
    }

    static BsDate fromLocalDate(LocalDate date) {
        long epochDay = ChronoUnit.DAYS.between(AD_EPOCH, date);
        long lastDay = YEAR_START_EPOCH_DAY[YEAR_START_EPOCH_DAY.length - 1];
        if (epochDay < 0 || epochDay >= lastDay) {
            throw new BsDateException("Date " + date + " is outside the supported BS range ["
                    + BsCalendarData.MIN_YEAR + ", " + BsCalendarData.MAX_YEAR + "]");
        }

        int year = findYear(epochDay);
        long daysIntoYear = epochDay - YEAR_START_EPOCH_DAY[year - BsCalendarData.MIN_YEAR];
        int[] monthLengths = BsCalendarData.monthLengths(year);
        int month = 1;
        while (daysIntoYear >= monthLengths[month - 1]) {
            daysIntoYear -= monthLengths[month - 1];
            month++;
        }
        return BsDate.of(year, month, (int) daysIntoYear + 1);
    }

    private static int findYear(long epochDay) {
        int lo = 0;
        int hi = BsCalendarData.MAX_YEAR - BsCalendarData.MIN_YEAR;
        while (lo < hi) {
            int mid = (lo + hi + 1) >>> 1;
            if (YEAR_START_EPOCH_DAY[mid] <= epochDay) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return BsCalendarData.MIN_YEAR + lo;
    }
}