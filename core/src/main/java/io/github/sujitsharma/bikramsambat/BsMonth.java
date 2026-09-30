package io.github.sujitsharma.bikramsambat;

/**
 * The 12 months of the Bikram Sambat calendar, in the order the BS year starts.
 */
public enum BsMonth {
    BAISHAKH(1),
    JESTHA(2),
    ASHADH(3),
    SHRAWAN(4),
    BHADRA(5),
    ASHWIN(6),
    KARTIK(7),
    MANGSIR(8),
    POUSH(9),
    MAGH(10),
    FALGUN(11),
    CHAITRA(12);

    private final int value;

    BsMonth(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BsMonth of(int month) {
        if (month < 1 || month > 12) {
            throw new BsDateException("Invalid BS month value: " + month + " (expected 1-12)");
        }
        return values()[month - 1];
    }

    /** Returns the month that is the given number of months after this one, wrapping around the year. */
    public BsMonth plus(long months) {
        int amount = (int) Math.floorMod(months, 12);
        return values()[(ordinal() + amount) % 12];
    }

    /** Returns the month that is the given number of months before this one, wrapping around the year. */
    public BsMonth minus(long months) {
        return plus(-Math.floorMod(months, 12));
    }
}