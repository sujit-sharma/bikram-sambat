package io.github.sujitsharma.bikramsambat.spring.autoconfigure;

import io.github.sujitsharma.bikramsambat.spring.BsDateMvcConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Enables {@code BsDate} request parameter conversion in Spring Boot MVC applications. */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(WebMvcConfigurer.class)
@Import(BsDateMvcConfiguration.class)
public class BsDateMvcAutoConfiguration {
}
