package io.github.sujitsharma.bikramsambat.spring.autoconfigure;

import io.github.sujitsharma.bikramsambat.jackson.v3.BsDateJackson3Module;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.JacksonModule;

/** Registers the Jackson 3 BsDate module when Jackson 3 is available. */
@AutoConfiguration
@Conditional(BsDateJacksonConditions.Jackson3Present.class)
public class BsDateJackson3AutoConfiguration {
    @Bean
    public JacksonModule bsDateJackson3Module() {
        return new BsDateJackson3Module();
    }
}
