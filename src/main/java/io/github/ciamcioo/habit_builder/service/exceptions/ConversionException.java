package io.github.ciamcioo.habit_builder.service.exceptions;

public class ConversionException extends RuntimeException{
    public static final String CONVERSION_EXCEPTION_MESSAGE_FORMAT = "Conversion of %s to %s ended up with failure!";

    public ConversionException(String fromObject, String toObject) {
        super(String.format(CONVERSION_EXCEPTION_MESSAGE_FORMAT, fromObject, toObject));
    }
}
