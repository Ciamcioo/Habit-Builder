package io.github.ciamcioo.habit_builder.model.dto;

import io.github.ciamcioo.habit_builder.model.commons.HabitFrequency;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Objects;

public record HabitDTO(

    @NotBlank(message = "Habit name cannot be blank")
    @Size(min = 3, max = 255, message = "Habit name must have 3 to 255 characters")
    String name,

    @NotNull(message = "Habit frequency cannot be null")
    HabitFrequency frequency,


    @FutureOrPresent(message = "Start date of habit cannot be placed in the past")
    LocalDate startDate,

    @Future(message = "End date of habit must be placed in the future")
    LocalDate endDate,

    Boolean reminder
) {

    public HabitDTO(String name,
                    HabitFrequency frequency,
                    LocalDate startDate,
                    LocalDate endDate,
                    Boolean reminder
    ) {

        this.name = name;
        this.frequency = frequency;
        this.startDate = startDate != null ? startDate : LocalDate.now();
        this.endDate = endDate != null ? endDate : LocalDate.now().plusYears(1);
        this.reminder = reminder != null ? reminder : false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HabitDTO habitDTO = (HabitDTO) o;
        return Objects.equals(name, habitDTO.name) && Objects.equals(reminder, habitDTO.reminder) && Objects.equals(endDate, habitDTO.endDate) && Objects.equals(startDate, habitDTO.startDate) && frequency == habitDTO.frequency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, frequency, startDate, endDate, reminder);
    }

    @Override
    public String toString() {
        return "HabitDTO{" +
                "name='" + name + '\'' +
                ", frequency=" + frequency +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reminder=" + reminder +
                '}';
    }
}

