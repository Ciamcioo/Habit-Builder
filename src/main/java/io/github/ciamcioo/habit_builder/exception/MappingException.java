package io.github.ciamcioo.habit_builder.exception;

public class MappingException extends RuntimeException{
    public static final String DEFAULT_MAPPING_EXCEPTION_MESSAGE = "An error occurred while mapping data: incompatible types or missing fields";

    @SuppressWarnings("unused")
    public MappingException() {
        super(DEFAULT_MAPPING_EXCEPTION_MESSAGE);
    }

    @SuppressWarnings("unused")
    public MappingException(String message) {
        super(message);
    }
}
