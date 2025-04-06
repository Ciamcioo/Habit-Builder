package io.github.ciamcioo.habit_builder.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public static final String DEFAULT_USER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "User already exists";

    @SuppressWarnings("unused")
    public UserAlreadyExistsException() {
        super(DEFAULT_USER_ALREADY_EXISTS_EXCEPTION_MESSAGE);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
