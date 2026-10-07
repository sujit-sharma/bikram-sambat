package io.github.sujitsharma.bikramsambat.jackson.v3;

import io.github.sujitsharma.bikramsambat.BsDate;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/** Deserializes a {@link BsDate} from a yyyy-MM-dd JSON string with Jackson 3. */
public final class BsDateDeserializer extends ValueDeserializer<BsDate> {
    @Override
    public BsDate deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        if (parser.currentToken() != JsonToken.VALUE_STRING) {
            return (BsDate) context.handleUnexpectedToken(BsDate.class, parser);
        }

        String text = parser.getString().trim();
        try {
            return BsDate.parse(text);
        } catch (RuntimeException exception) {
            return (BsDate) context.handleWeirdStringValue(
                    BsDate.class, text, "Invalid BS date; expected a valid yyyy-MM-dd date");
        }
    }
}
