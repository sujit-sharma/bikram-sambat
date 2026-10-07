package io.github.sujitsharma.bikramsambat.jackson.v3;

import io.github.sujitsharma.bikramsambat.BsDate;
import tools.jackson.databind.module.SimpleModule;

/** Jackson 3 module for serializing {@link BsDate} as a yyyy-MM-dd string. */
public final class BsDateJackson3Module extends SimpleModule {
    private static final long serialVersionUID = 1L;

    public BsDateJackson3Module() {
        super("BsDateJackson3Module");
        addSerializer(BsDate.class, new BsDateSerializer());
        addDeserializer(BsDate.class, new BsDateDeserializer());
    }
}
