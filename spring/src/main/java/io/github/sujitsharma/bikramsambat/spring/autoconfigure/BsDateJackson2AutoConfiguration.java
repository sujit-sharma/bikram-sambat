package io.github.sujitsharma.bikramsambat.spring.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import io.github.sujitsharma.bikramsambat.jackson.BsDateJacksonModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Bean;

/** Registers the Jackson 2 BsDate module when Jackson 2 is the available version. */
@AutoConfiguration
@Conditional(BsDateJacksonConditions.Jackson2Only.class)
public class BsDateJackson2AutoConfiguration {
    @Bean
    public Module bsDateJackson2Module() {
        return new BsDateJacksonModule();
    }
}
