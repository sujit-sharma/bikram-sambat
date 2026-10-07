package io.github.sujitsharma.bikramsambat.spring.autoconfigure;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

/** Detects the servlet namespace used by Spring Boot 2 and Spring Boot 3+. */
public final class BsDateSpringVersionConditions {
    private static final String SERVLET_V2 = "javax.servlet.Servlet";
    private static final String SERVLET_V3 = "jakarta.servlet.Servlet";

    private BsDateSpringVersionConditions() {}

    public static final class Boot2 implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ClassLoader classLoader = context.getClassLoader();
            return ClassUtils.isPresent(SERVLET_V2, classLoader)
                    && !ClassUtils.isPresent(SERVLET_V3, classLoader);
        }
    }

    public static final class Boot3OrLater implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ClassUtils.isPresent(SERVLET_V3, context.getClassLoader());
        }
    }
}
