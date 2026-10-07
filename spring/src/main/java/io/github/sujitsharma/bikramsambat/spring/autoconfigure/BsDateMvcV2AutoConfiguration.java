package io.github.sujitsharma.bikramsambat.spring.autoconfigure;

import io.github.sujitsharma.bikramsambat.spring.BsDateMvcConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Enables MVC date conversion in Spring Boot 2 applications using javax.servlet. */
@Configuration
@Conditional(BsDateSpringVersionConditions.Boot2.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(WebMvcConfigurer.class)
@Import(BsDateMvcConfiguration.class)
public class BsDateMvcV2AutoConfiguration {
}
