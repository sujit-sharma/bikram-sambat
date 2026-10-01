package io.github.sujitsharma.bikramsambat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BsMonthTest {

    @Test
    void ofMapsValueToEnumConstant() {
        assertEquals(BsMonth.BAISHAKH, BsMonth.of(1));
        assertEquals(BsMonth.CHAITRA, BsMonth.of(12));
        assertThrows(BsDateException.class, () -> BsMonth.of(0));
        assertThrows(BsDateException.class, () -> BsMonth.of(13));
    }

    @Test
    void plusWrapsAroundYearEnd() {
        assertEquals(BsMonth.BAISHAKH, BsMonth.CHAITRA.plus(1));
        assertEquals(BsMonth.JESTHA, BsMonth.CHAITRA.plus(2));
    }

    @Test
    void minusWrapsAroundYearStart() {
        assertEquals(BsMonth.CHAITRA, BsMonth.BAISHAKH.minus(1));
        assertEquals(BsMonth.FALGUN, BsMonth.BAISHAKH.minus(2));
    }
}