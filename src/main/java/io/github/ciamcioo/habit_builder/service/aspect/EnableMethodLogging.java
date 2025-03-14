package io.github.ciamcioo.habit_builder.service.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EnableReturnLogging
@EnableMethodCallLogging
public @interface EnableMethodLogging {
}
