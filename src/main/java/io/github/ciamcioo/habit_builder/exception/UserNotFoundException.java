package io.github.ciamcioo.habit_builder.exception;

public class UserNotFoundException extends RuntimeException {
    public static final String DEFAULT_USER_NOT_FOUND_EXCEPTION_MESSAGE = "User with given attributes not found";


    @SuppressWarnings("unused")
    public UserNotFoundException() {
        super(DEFAULT_USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
