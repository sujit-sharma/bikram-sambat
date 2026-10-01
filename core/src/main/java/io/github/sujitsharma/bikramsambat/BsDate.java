package io.github.sujitsharma.bikramsambat;

import java.io.Serializable;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * A date in the Bikram Sambat (BS) calendar, e.g. {@code 2083-06-13}.
 *
 * <p>Modeled after {@link java.time.LocalDate}: immutable, comparable, and
 * always representing a valid BS calendar date. Supported years range from
 * {@code MIN.getYear()} to {@code MAX.getYear()}.
 */
public final class BsDate implements Comparable<BsDate>, Serializable {

    private static final long serialVersionUID = 1L;

    /** The earliest supported BS date. */
    public static final BsDate MIN = new BsDate(BsCalendarData.MIN_YEAR, 1, 1);

    /** The latest supported BS date. */
    public static final BsDate MAX = new BsDate(BsCalendarData.MAX_YEAR, 12,
            BsCalendarData.lengthOfMonth(BsCalendarData.MAX_YEAR, 12));

    private final int year;
    private final int month;
    private final int day;

    private BsDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static BsDate of(int year, int month, int day) {
        BsCalendarData.validateYear(year);
        if (month < 1 || month > 12) {
            throw new BsDateException("Invalid BS month value: " + month + " (expected 1-12)");
        }
        int maxDay = BsCalendarData.lengthOfMonth(year, month);
        if (day < 1 || day > maxDay) {
            throw new BsDateException(
                    "Invalid day of month: " + day + " for " + year + "-" + month + " (expected 1-" + maxDay + ")");
        }
        return new BsDate(year, month, day);
    }

    public static BsDate of(int year, BsMonth month, int day) {
        Objects.requireNonNull(month, "month");
        return of(year, month.getValue(), day);
    }

    /**
     * Creates a BS date from a year and a day-of-year (1-based), e.g.
     * {@code ofYearDay(2082, 1)} is the same as {@code of(2082, 1, 1)}.
     */
    public static BsDate ofYearDay(int year, int dayOfYear) {
        BsCalendarData.validateYear(year);
        int lengthOfYear = BsCalendarData.lengthOfYear(year);
        if (dayOfYear < 1 || dayOfYear > lengthOfYear) {
            throw new BsDateException(
                    "Invalid day of year: " + dayOfYear + " for " + year + " (expected 1-" + lengthOfYear + ")");
        }
        int[] monthLengths = BsCalendarData.monthLengths(year);
        int month = 1;
        int remaining = dayOfYear;
        while (remaining > monthLengths[month - 1]) {
            remaining -= monthLengths[month - 1];
            month++;
        }
        return new BsDate(year, month, remaining);
    }

    /**
     * Creates a BS date from an epoch day, i.e. the number of days since
     * {@code BsDate.MIN} ({@code MIN} itself being epoch day {@code 0}).
     */
    public static BsDate ofEpochDay(long epochDay) {
        return BsEpochConverter.fromEpochDay(epochDay);
    }

    /** Returns today's BS date in the system default timezone. */
    public static BsDate now() {
        return from(LocalDate.now());
    }

    /** Returns today's BS date in the specified timezone. */
    public static BsDate now(ZoneId zone) {
        return from(LocalDate.now(zone));
    }

    public static BsDate now(Clock clock) {
        return from(LocalDate.now(clock));
    }

    /**
     * Creates a BS date from a Gregorian calendar date. No timezone conversion
     * is performed; use {@link LocalDate#of(int, int, int)} after converting an
     * instant to the desired timezone when starting from a timestamp.
     */
    public static BsDate from(LocalDate localDate) {
        Objects.requireNonNull(localDate, "localDate");
        return BsEpochConverter.fromLocalDate(localDate);
    }

    /**
     * Parses a BS date in {@code yyyy-MM-dd} form, e.g. {@code "2083-06-13"}.
     */
    public static BsDate parse(CharSequence text) {
        Objects.requireNonNull(text, "text");
        String[] parts = text.toString().split("-");
        if (parts.length != 3) {
            throw new BsDateException("Cannot parse BS date: '" + text + "' (expected yyyy-MM-dd)");
        }
        try {
            int y = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int d = Integer.parseInt(parts[2]);
            return of(y, m, d);
        } catch (NumberFormatException e) {
            throw new BsDateException("Cannot parse BS date: '" + text + "' (expected yyyy-MM-dd)");
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonthValue() {
        return month;
    }

    public BsMonth getMonth() {
        return BsMonth.of(month);
    }

    public int getDayOfMonth() {
        return day;
    }

    public int getDayOfYear() {
        int[] monthLengths = BsCalendarData.monthLengths(year);
        int total = day;
        for (int m = 1; m < month; m++) {
            total += monthLengths[m - 1];
        }
        return total;
    }

    public DayOfWeek getDayOfWeek() {
        return toLocalDate().getDayOfWeek();
    }

    public int lengthOfMonth() {
        return BsCalendarData.lengthOfMonth(year, month);
    }

    public int lengthOfYear() {
        return BsCalendarData.lengthOfYear(year);
    }

    public boolean isLeapYear() {
        return lengthOfYear() == 366;
    }

    /**
     * Returns the Gregorian calendar date corresponding to this BS date.
     * The result is timezone-free, just like {@link LocalDate}.
     */
    public LocalDate toLocalDate() {
        return BsEpochConverter.toLocalDate(year, month, day);
    }

    public long toEpochDay() {
        return BsEpochConverter.toEpochDay(year, month, day);
    }

    public BsDate plusDays(long daysToAdd) {
        if (daysToAdd == 0) {
            return this;
        }
        return BsEpochConverter.fromLocalDate(toLocalDate().plusDays(daysToAdd));
    }

    public BsDate minusDays(long daysToSubtract) {
        return plusDays(Math.negateExact(daysToSubtract));
    }

    public BsDate plusWeeks(long weeksToAdd) {
        return plusDays(Math.multiplyExact(weeksToAdd, 7));
    }

    public BsDate minusWeeks(long weeksToSubtract) {
        return plusWeeks(Math.negateExact(weeksToSubtract));
    }

    /** Zero-based count of months since {@code BsCalendarData.MIN_YEAR}-01, used for month/period arithmetic. */
    long prolepticMonth() {
        return (long) (year - BsCalendarData.MIN_YEAR) * 12 + (month - 1);
    }

    public BsDate plusMonths(long monthsToAdd) {
        if (monthsToAdd == 0) {
            return this;
        }
        // The adjustment is a long, so guard the addition before converting it
        // back to the supported year range. Plain addition could wrap for
        // extreme inputs such as Long.MAX_VALUE.
        long totalMonths = Math.addExact(prolepticMonth(), monthsToAdd);
        int newYear = BsCalendarData.MIN_YEAR + Math.toIntExact(Math.floorDiv(totalMonths, 12));
        int newMonth = Math.toIntExact(Math.floorMod(totalMonths, 12)) + 1;
        BsCalendarData.validateYear(newYear);
        int newDay = Math.min(day, BsCalendarData.lengthOfMonth(newYear, newMonth));
        return new BsDate(newYear, newMonth, newDay);
    }

    public BsDate minusMonths(long monthsToSubtract) {
        return plusMonths(Math.negateExact(monthsToSubtract));
    }

    public BsDate plusYears(long yearsToAdd) {
        if (yearsToAdd == 0) {
            return this;
        }
        int newYear = Math.toIntExact(Math.addExact((long) year, yearsToAdd));
        BsCalendarData.validateYear(newYear);
        int newDay = Math.min(day, BsCalendarData.lengthOfMonth(newYear, month));
        return new BsDate(newYear, month, newDay);
    }

    public BsDate minusYears(long yearsToSubtract) {
        return plusYears(Math.negateExact(yearsToSubtract));
    }

    /** Returns a copy of this date with the year altered, clamping the day if it no longer exists. */
    public BsDate withYear(int newYear) {
        if (newYear == year) {
            return this;
        }
        BsCalendarData.validateYear(newYear);
        int newDay = Math.min(day, BsCalendarData.lengthOfMonth(newYear, month));
        return new BsDate(newYear, month, newDay);
    }

    /** Returns a copy of this date with the month-of-year altered, clamping the day if it no longer exists. */
    public BsDate withMonth(int newMonth) {
        if (newMonth == month) {
            return this;
        }
        if (newMonth < 1 || newMonth > 12) {
            throw new BsDateException("Invalid BS month value: " + newMonth + " (expected 1-12)");
        }
        int newDay = Math.min(day, BsCalendarData.lengthOfMonth(year, newMonth));
        return new BsDate(year, newMonth, newDay);
    }

    public BsDate withMonth(BsMonth newMonth) {
        Objects.requireNonNull(newMonth, "newMonth");
        return withMonth(newMonth.getValue());
    }

    public BsDate withDayOfMonth(int newDayOfMonth) {
        if (newDayOfMonth == day) {
            return this;
        }
        return of(year, month, newDayOfMonth);
    }

    public BsDate withDayOfYear(int newDayOfYear) {
        return ofYearDay(year, newDayOfYear);
    }

    public boolean isBefore(BsDate other) {
        return compareTo(other) < 0;
    }

    public boolean isAfter(BsDate other) {
        return compareTo(other) > 0;
    }

    public boolean isEqual(BsDate other) {
        return compareTo(other) == 0;
    }

    /** Returns the first day of this date's BS month. */
    public BsDate firstDayOfMonth() {
        return withDayOfMonth(1);
    }

    /** Returns the last day of this date's BS month. */
    public BsDate lastDayOfMonth() {
        return withDayOfMonth(lengthOfMonth());
    }

    /** Returns the first day of the BS month after this date's month. */
    public BsDate firstDayOfNextMonth() {
        return plusMonths(1).withDayOfMonth(1);
    }

    /** Returns the first day of this date's BS year. */
    public BsDate firstDayOfYear() {
        return withDayOfYear(1);
    }

    /** Returns the last day of this date's BS year. */
    public BsDate lastDayOfYear() {
        return of(year, 12, BsCalendarData.lengthOfMonth(year, 12));
    }

    /** Returns the first day of the BS year after this date's year. */
    public BsDate firstDayOfNextYear() {
        return of(year + 1, 1, 1);
    }

    /**
     * Computes the amount of time until another BS date in terms of the given
     * unit. Supports {@link ChronoUnit#DAYS}, {@link ChronoUnit#MONTHS} and
     * {@link ChronoUnit#YEARS}, mirroring {@link LocalDate#until(java.time.temporal.Temporal, java.time.temporal.TemporalUnit)}.
     */
    public long until(BsDate endExclusive, ChronoUnit unit) {
        Objects.requireNonNull(endExclusive, "endExclusive");
        Objects.requireNonNull(unit, "unit");
        switch (unit) {
            case DAYS:
                return endExclusive.toEpochDay() - toEpochDay();
            case MONTHS:
                return monthsUntil(endExclusive);
            case YEARS:
                return monthsUntil(endExclusive) / 12;
            default:
                throw new UnsupportedOperationException("Unsupported unit: " + unit);
        }
    }

    private long monthsUntil(BsDate end) {
        long months = end.prolepticMonth() - prolepticMonth();
        if (months > 0 && end.day < day) {
            months--;
        } else if (months < 0 && end.day > day) {
            months++;
        }
        return months;
    }

    /**
     * Computes the years/months/days breakdown between this date and another,
     * mirroring {@link LocalDate#until(java.time.chrono.ChronoLocalDate)}.
     */
    public BsPeriod until(BsDate endDateExclusive) {
        return BsPeriod.between(this, endDateExclusive);
    }

    /**
     * Formats this date using a small pattern vocabulary: {@code yyyy}, {@code MM},
     * {@code dd} and {@code MMMM} (full BS month name).
     */
    public String format(String pattern) {
        Objects.requireNonNull(pattern, "pattern");
        return pattern
                .replace("yyyy", String.format("%04d", year))
                .replace("MMMM", getMonth().name())
                .replace("MM", String.format("%02d", month))
                .replace("dd", String.format("%02d", day));
    }

    @Override
    public int compareTo(BsDate other) {
        int cmp = Integer.compare(year, other.year);
        if (cmp == 0) {
            cmp = Integer.compare(month, other.month);
        }
        if (cmp == 0) {
            cmp = Integer.compare(day, other.day);
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BsDate)) {
            return false;
        }
        BsDate other = (BsDate) o;
        return year == other.year && month == other.month && day == other.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
