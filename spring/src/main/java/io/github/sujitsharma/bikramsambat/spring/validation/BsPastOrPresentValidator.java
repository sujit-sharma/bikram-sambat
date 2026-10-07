package io.github.sujitsharma.bikramsambat.spring.validation;

import io.github.sujitsharma.bikramsambat.BsDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class BsPastOrPresentValidator implements ConstraintValidator<BsPastOrPresent, BsDate> {
    @Override
    public boolean isValid(BsDate value, ConstraintValidatorContext context) {
        return BsDateTemporalValidator.isValid(value, context, BsDateTemporalValidator.Relation.PAST_OR_PRESENT);
    }
}
