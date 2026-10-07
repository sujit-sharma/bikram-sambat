package io.github.sujitsharma.bikramsambat.spring;

import io.github.sujitsharma.bikramsambat.BsDate;
import org.springframework.core.convert.converter.Converter;

/** Formats a {@link BsDate} as a {@code yyyy-MM-dd} string. */
public final class BsDateToStringConverter implements Converter<BsDate, String> {

    @Override
    public String convert(BsDate source) {
        return source.toString();
    }
}
