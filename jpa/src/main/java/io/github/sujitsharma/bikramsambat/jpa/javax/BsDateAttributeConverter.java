package io.github.sujitsharma.bikramsambat.jpa.javax;

import io.github.sujitsharma.bikramsambat.BsDate;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/** JPA 2 (javax.persistence) converter for storing {@link BsDate} as SQL DATE. */
@Converter(autoApply = true)
public class BsDateAttributeConverter implements AttributeConverter<BsDate, LocalDate> {
    @Override
    public LocalDate convertToDatabaseColumn(BsDate attribute) {
        return attribute == null ? null : attribute.toLocalDate();
    }

    @Override
    public BsDate convertToEntityAttribute(LocalDate databaseValue) {
        return databaseValue == null ? null : BsDate.from(databaseValue);
    }
}
