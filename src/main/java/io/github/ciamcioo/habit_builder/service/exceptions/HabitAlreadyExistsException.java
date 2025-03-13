package io.github.ciamcioo.habit_builder.service.exceptions;

public class HabitAlreadyExistsException extends RuntimeException {
    public static final String DEF_MESSAGE = "Such a habit already exists in the habit set";

    public HabitAlreadyExistsException() {
        super(DEF_MESSAGE);
    }
    public HabitAlreadyExistsException(String message) {
        super(message);
    }
}
