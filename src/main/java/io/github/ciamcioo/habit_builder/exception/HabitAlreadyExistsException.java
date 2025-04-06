package io.github.ciamcioo.habit_builder.exception;

public class HabitAlreadyExistsException extends RuntimeException {
    public static final String DEFAULT_HABIT_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Habit already exists";

    public HabitAlreadyExistsException() {
        super(DEFAULT_HABIT_ALREADY_EXISTS_EXCEPTION_MESSAGE);
    }
    public HabitAlreadyExistsException(String message) {
        super(message);
    }
}
