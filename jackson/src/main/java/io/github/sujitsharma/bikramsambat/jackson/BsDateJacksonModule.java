package io.github.sujitsharma.bikramsambat.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.sujitsharma.bikramsambat.BsDate;

/** Jackson module that encodes {@link BsDate} values as {@code yyyy-MM-dd} strings. */
public final class BsDateJacksonModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public BsDateJacksonModule() {
        super("BsDateJacksonModule");
        addSerializer(BsDate.class, new BsDateSerializer());
        addDeserializer(BsDate.class, new BsDateDeserializer());
    }
}
