package io.github.ciamcioo.habit_builder.exceptions;

public class HabitNotFoundException extends RuntimeException {

    public HabitNotFoundException(String message) {
        super(message);
    }

    public HabitNotFoundException(String messageFormat, String argumentName , String argumentValue) {
        super(String.format(messageFormat, argumentName, argumentValue));
    }
}
