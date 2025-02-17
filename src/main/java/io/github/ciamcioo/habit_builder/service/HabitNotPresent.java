package io.github.ciamcioo.habit_builder.service;

public class HabitNotPresent extends RuntimeException {

    public HabitNotPresent(String message) {
        super(message);
    }

    public HabitNotPresent(String messageFormat, String argumentName , String argumentValue) {
        super(String.format(messageFormat, argumentName, argumentValue));
    }
}
