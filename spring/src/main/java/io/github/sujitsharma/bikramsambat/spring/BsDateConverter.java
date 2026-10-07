package io.github.sujitsharma.bikramsambat.spring;

import io.github.sujitsharma.bikramsambat.BsDate;
import org.springframework.core.convert.converter.Converter;

/** Converts request strings in {@code yyyy-MM-dd} form to {@link BsDate}. */
public final class BsDateConverter implements Converter<String, BsDate> {

    @Override
    public BsDate convert(String source) {
        return BsDate.parse(source);
    }
}
