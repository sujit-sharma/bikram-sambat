package io.github.sujitsharma.bikramsambat.spring.validation.v2;

import io.github.sujitsharma.bikramsambat.BsDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class BsPastValidator implements ConstraintValidator<BsPast, BsDate> {
    @Override
    public boolean isValid(BsDate value, ConstraintValidatorContext context) {
        return BsDateTemporalValidator.isValid(value, context.getClockProvider().getClock(),
                BsDateTemporalValidator.Relation.PAST);
    }
}
