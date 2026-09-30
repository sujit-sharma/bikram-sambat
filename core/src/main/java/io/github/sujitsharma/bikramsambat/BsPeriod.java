package io.github.sujitsharma.bikramsambat;

import java.io.Serializable;
import java.util.Objects;

/**
 * A date-based amount of time in the BS calendar, expressed as years, months
 * and days, e.g. "2 years, 3 months and 4 days". Mirrors {@link java.time.Period}.
 */
public final class BsPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final BsPeriod ZERO = new BsPeriod(0, 0, 0);

    private final int years;
    private final int months;
    private final int days;

    private BsPeriod(int years, int months, int days) {
        this.years = years;
        this.months = months;
        this.days = days;
    }

    public static BsPeriod of(int years, int months, int days) {
        if (years == 0 && months == 0 && days == 0) {
            return ZERO;
        }
        return new BsPeriod(years, months, days);
    }

    public static BsPeriod ofYears(int years) {
        return of(years, 0, 0);
    }

    public static BsPeriod ofMonths(int months) {
        return of(0, months, 0);
    }

    public static BsPeriod ofDays(int days) {
        return of(0, 0, days);
    }

    public static BsPeriod zero() {
        return ZERO;
    }

    /**
     * Computes the period between two BS dates, in the same year/month/day
     * breakdown style as {@link java.time.LocalDate#until(java.time.chrono.ChronoLocalDate)}.
     */
    public static BsPeriod between(BsDate startInclusive, BsDate endExclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        Objects.requireNonNull(endExclusive, "endExclusive");

        long totalMonths = endExclusive.prolepticMonth() - startInclusive.prolepticMonth();
        int days = endExclusive.getDayOfMonth() - startInclusive.getDayOfMonth();
        if (totalMonths > 0 && days < 0) {
            totalMonths--;
            BsDate calcDate = startInclusive.plusMonths(totalMonths);
            days = (int) (endExclusive.toEpochDay() - calcDate.toEpochDay());
        } else if (totalMonths < 0 && days > 0) {
            totalMonths++;
            days -= endExclusive.lengthOfMonth();
        }
        long years = totalMonths / 12;
        int months = (int) (totalMonths % 12);
        return of(Math.toIntExact(years), months, days);
    }

    public int getYears() {
        return years;
    }

    public int getMonths() {
        return months;
    }

    public int getDays() {
        return days;
    }

    public boolean isZero() {
        return years == 0 && months == 0 && days == 0;
    }

    public boolean isNegative() {
        return years < 0 || months < 0 || days < 0;
    }

    public long toTotalMonths() {
        return years * 12L + months;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BsPeriod)) {
            return false;
        }
        BsPeriod other = (BsPeriod) o;
        return years == other.years && months == other.months && days == other.days;
    }

    @Override
    public int hashCode() {
        return Objects.hash(years, months, days);
    }

    @Override
    public String toString() {
        if (isZero()) {
            return "P0D";
        }
        StringBuilder sb = new StringBuilder("P");
        if (years != 0) {
            sb.append(years).append('Y');
        }
        if (months != 0) {
            sb.append(months).append('M');
        }
        if (days != 0) {
            sb.append(days).append('D');
        }
        return sb.toString();
    }
}