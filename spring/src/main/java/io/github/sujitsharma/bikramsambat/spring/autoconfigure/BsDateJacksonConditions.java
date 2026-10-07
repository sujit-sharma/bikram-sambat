package io.github.sujitsharma.bikramsambat.spring.autoconfigure;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

/** Mutually exclusive classpath checks for the Jackson module auto-configurations. */
public final class BsDateJacksonConditions {
    private static final String JACKSON_2_MAPPER = "com.fasterxml.jackson.databind.ObjectMapper";
    private static final String JACKSON_3_MAPPER = "tools.jackson.databind.ObjectMapper";

    private BsDateJacksonConditions() {}

    public static final class Jackson2Only implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ClassLoader classLoader = context.getClassLoader();
            return ClassUtils.isPresent(JACKSON_2_MAPPER, classLoader)
                    && !ClassUtils.isPresent(JACKSON_3_MAPPER, classLoader);
        }
    }

    public static final class Jackson3Present implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ClassUtils.isPresent(JACKSON_3_MAPPER, context.getClassLoader());
        }
    }
}
