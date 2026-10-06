package io.github.sujitsharma.bikramsambat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/** Serializes a {@link BsDate} as an ISO-style {@code yyyy-MM-dd} JSON string. */
public final class BsDateSerializer extends JsonSerializer<BsDate> {

    @Override
    public void serialize(BsDate value, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
        generator.writeString(value.toString());
    }
}
