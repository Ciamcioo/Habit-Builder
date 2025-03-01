package io.github.ciamcioo.habit_builder.commons;

import io.github.ciamcioo.habit_builder.dto.HabitDto;
import io.github.ciamcioo.habit_builder.entity.Habit;

import java.time.LocalDate;
import java.util.UUID;

public class HabitBuilder {

    private UUID uuid;
    private String name;
    private HabitFrequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean reminder;

    public static HabitBuilder getInstance() {
        return new HabitBuilder();
    }

    public HabitBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public HabitBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HabitBuilder withFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
        return this;
    }

    public HabitBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public HabitBuilder withEndDate(LocalDate endDate) {
        this.endDate= endDate;
        return this;
    }

    public HabitBuilder withReminder(Boolean reminder) {
        this.reminder = reminder;
        return this;
    }

    public HabitBuilder withTestValues() {
        uuid = UUID.randomUUID();
        name = "Foo_habit";
        frequency = HabitFrequency.DAILY;
        startDate = LocalDate.now().minusDays(1);
        endDate = LocalDate.now().plusYears(1);
        reminder = true;

        return this;
    }


    public HabitDto buildHabitDto() {
        return new HabitDto(name,
                            frequency,
                            startDate,
                            endDate,
                            reminder
        );
    }

    public Habit buildHabit() {
        return new Habit(uuid,
                        name,
                        frequency,
                        startDate,
                        endDate,
                        reminder
        );
    }
}
