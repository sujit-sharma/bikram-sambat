package io.github.sujitsharma.bikramsambat.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Registers {@code BsDate} string and Gregorian date conversions with Spring MVC. */
@Configuration(proxyBeanMethods = false)
public class BsDateMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new BsDateConverter());
        registry.addConverter(new BsDateToStringConverter());
        registry.addConverter(new LocalDateToBsDateConverter());
        registry.addConverter(new BsDateToLocalDateConverter());
    }
}
