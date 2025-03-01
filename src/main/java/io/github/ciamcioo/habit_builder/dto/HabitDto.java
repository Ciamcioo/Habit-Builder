package io.github.ciamcioo.habit_builder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ciamcioo.habit_builder.commons.HabitFrequency;

import java.time.LocalDate;
import java.util.Objects;

public record HabitDto(
    String name,
    HabitFrequency frequency,
    LocalDate startDate,
    LocalDate endDate,
    Boolean reminder
) {

    @JsonCreator
    public HabitDto(@JsonProperty("name") String name,
                    @JsonProperty("frequency") HabitFrequency frequency) {
        this(name,
            frequency,
            LocalDate.now(),
            LocalDate.now().plusYears(1),
            false
        );
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

