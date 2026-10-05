package io.github.sujitsharma.bikramsambat.jpa;

import io.github.sujitsharma.bikramsambat.BsDate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;

/**
 * Converts {@link BsDate} values to their equivalent Gregorian {@link LocalDate}
 * for storage in a SQL {@code DATE} column, and back when reading them.
 *
 * <p>The converter is applied automatically to mapped {@code BsDate} attributes.
 * JPA providers such as Hibernate map {@code LocalDate} to SQL {@code DATE}.
 */
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
