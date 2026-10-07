package io.github.sujitsharma.bikramsambat.spring.validation;

import io.github.sujitsharma.bikramsambat.BsDate;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Clock;

final class BsDateTemporalValidator {
    private BsDateTemporalValidator() {}

    static boolean isValid(BsDate value, ConstraintValidatorContext context, Relation relation) {
        if (value == null) {
            return true;
        }
        Clock clock = context.getClockProvider().getClock();
        BsDate today = BsDate.now(clock);
        int comparison = value.compareTo(today);
        return switch (relation) {
            case PAST -> comparison < 0;
            case PAST_OR_PRESENT -> comparison <= 0;
            case FUTURE -> comparison > 0;
            case FUTURE_OR_PRESENT -> comparison >= 0;
        };
    }

    enum Relation { PAST, PAST_OR_PRESENT, FUTURE, FUTURE_OR_PRESENT }
}
