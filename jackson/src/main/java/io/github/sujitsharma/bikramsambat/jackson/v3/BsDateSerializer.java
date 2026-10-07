package io.github.sujitsharma.bikramsambat.jackson.v3;

import io.github.sujitsharma.bikramsambat.BsDate;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/** Serializes a {@link BsDate} as a yyyy-MM-dd JSON string with Jackson 3. */
public final class BsDateSerializer extends ValueSerializer<BsDate> {
    @Override
    public void serialize(BsDate value, JsonGenerator generator, SerializationContext context) throws JacksonException {
        generator.writeString(value.toString());
    }
}
