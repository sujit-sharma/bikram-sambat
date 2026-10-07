package io.github.sujitsharma.bikramsambat.spring;

import io.github.sujitsharma.bikramsambat.BsDate;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/** Converts a BS date to its corresponding Gregorian {@link LocalDate}. */
public final class BsDateToLocalDateConverter implements Converter<BsDate, LocalDate> {

    @Override
    public LocalDate convert(BsDate source) {
        return source.toLocalDate();
    }
}
