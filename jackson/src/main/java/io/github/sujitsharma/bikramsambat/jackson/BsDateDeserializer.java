package io.github.sujitsharma.bikramsambat.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.github.sujitsharma.bikramsambat.BsDate;

import java.io.IOException;

/** Deserializes a {@link BsDate} from a {@code yyyy-MM-dd} JSON string. */
public final class BsDateDeserializer extends JsonDeserializer<BsDate> {

    @Override
    public BsDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (!parser.hasToken(JsonToken.VALUE_STRING)) {
            return (BsDate) context.handleUnexpectedToken(BsDate.class, parser);
        }

        String text = parser.getText().trim();
        try {
            return BsDate.parse(text);
        } catch (RuntimeException e) {
            throw JsonMappingException.from(parser, "Invalid BS date; expected a valid yyyy-MM-dd date", e);
        }
    }
}
