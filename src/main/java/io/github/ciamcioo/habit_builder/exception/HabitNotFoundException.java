package io.github.ciamcioo.habit_builder.exception;

public class HabitNotFoundException extends RuntimeException {
   public static final String DEFAULT_NOT_FOUND_EXCEPTION_MESSAGE = "Habit with given attributes not found";

   public HabitNotFoundException() {
        super(DEFAULT_NOT_FOUND_EXCEPTION_MESSAGE);
   }

    public HabitNotFoundException(String message) {
        super(message);
    }
}
