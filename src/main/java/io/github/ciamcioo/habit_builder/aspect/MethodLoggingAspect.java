package io.github.ciamcioo.habit_builder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MethodLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(MethodLoggingAspect.class);


    @Before("@annotation(io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodCallLogging) || " +
            "@annotation(io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodLogging)")
    public void logMethodCall(JoinPoint joinPoint) {
        log.info("Event: {}, Method: {}, Arguments: {}",
                joinPoint.getKind(),
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs()
        );
    }

    @AfterReturning(value = "@annotation(io.github.ciamcioo.habit_builder.aspect.annotation.EnableReturnLogging) ||" +
                    "@annotation(io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodLogging)",
                    returning="returnValue")
    public void logMethodReturn(JoinPoint joinPoint, Object returnValue) {
        log.info("Event: {}, Method: {}, Returned object hashcode: {}",
                "Method return",
                joinPoint.getSignature().toShortString(),
                returnValue.hashCode()
        );

    }

    @AfterThrowing(value = "@annotation(io.github.ciamcioo.habit_builder.aspect.annotation.EnableExceptionLogging)", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        log.warn("Event: {}, Method: {}, Exception type: {}, Exception message: {}",
                 "Exception thrown",
                 joinPoint.toShortString(),
                 exception.getCause(),
                 exception.getMessage()
        );
    }
}
