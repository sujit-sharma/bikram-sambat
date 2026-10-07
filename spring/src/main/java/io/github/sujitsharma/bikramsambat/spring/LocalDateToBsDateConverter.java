package io.github.sujitsharma.bikramsambat.spring;

import io.github.sujitsharma.bikramsambat.BsDate;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/** Converts a Gregorian {@link LocalDate} to its corresponding BS date. */
public final class LocalDateToBsDateConverter implements Converter<LocalDate, BsDate> {

    @Override
    public BsDate convert(LocalDate source) {
        return BsDate.from(source);
    }
}
