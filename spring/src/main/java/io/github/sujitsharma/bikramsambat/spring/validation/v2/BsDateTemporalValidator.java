package io.github.sujitsharma.bikramsambat.spring.validation.v2;

import io.github.sujitsharma.bikramsambat.BsDate;
import java.time.Clock;

final class BsDateTemporalValidator {
    private BsDateTemporalValidator() {}

    static boolean isValid(BsDate value, Clock clock, Relation relation) {
        if (value == null) {
            return true;
        }
        int comparison = value.compareTo(BsDate.now(clock));
        switch (relation) {
            case PAST: return comparison < 0;
            case PAST_OR_PRESENT: return comparison <= 0;
            case FUTURE: return comparison > 0;
            case FUTURE_OR_PRESENT: return comparison >= 0;
            default: throw new IllegalStateException("Unknown temporal relation: " + relation);
        }
    }

    enum Relation { PAST, PAST_OR_PRESENT, FUTURE, FUTURE_OR_PRESENT }
}
