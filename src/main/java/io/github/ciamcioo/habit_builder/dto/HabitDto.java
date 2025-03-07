package io.github.ciamcioo.habit_builder.dto;

import io.github.ciamcioo.habit_builder.commons.HabitFrequency;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Objects;

public record HabitDto(

    @NotNull(message = "Habit name cannot be null")
    @Size(min = 1, max = 255, message = "Habit name must have 1 to 255 characters")
    String name,

    @NotNull(message = "Habit frequency cannot be null")
    HabitFrequency frequency,


    @FutureOrPresent(message = "Start date of habit cannot be placed in the past")
    LocalDate startDate,

    @Future(message = "End date of habit must be placed in the future")
    LocalDate endDate,
    Boolean reminder
) {

    public HabitDto(String name,
                    HabitFrequency frequency,
                    LocalDate startDate,
                    LocalDate endDate,
                    Boolean reminder) {

        this.name = name;
        this.frequency = frequency;
        this.startDate = startDate != null ? startDate : LocalDate.now();
        this.endDate = endDate != null ? endDate : LocalDate.now().plusYears(1);
        this.reminder = reminder != null ? reminder : false;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HabitDto habitDto = (HabitDto) o;
        return Objects.equals(name, habitDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}

