package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.commons.HabitFrequency;
import io.github.ciamcioo.habit_builder.entity.Habit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class HabitTestBuilder {

    private Integer id;
    private String name;
    private HabitFrequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean reminder;

    public static HabitTestBuilder getInstance() {
        return new HabitTestBuilder();
    }

    public HabitTestBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public HabitTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HabitTestBuilder withFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
        return this;
    }

    public HabitTestBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public HabitTestBuilder withEndDate(LocalDate endDate) {
        this.endDate= endDate;
        return this;
    }

    public HabitTestBuilder withReminder(Boolean reminder) {
        this.reminder = reminder;
        return this;
    }

    public HabitTestBuilder withTestValues() {
        id = 1;
        name = "Foo_habit";
        frequency = HabitFrequency.DAILY;
        startDate = LocalDate.now().minusDays(1);
        endDate = LocalDate.now().plusYears(1);
        reminder = true;

        return this;
    }


    public Habit build() {
        Habit habit = new Habit();
        habit.setId(id);
        habit.setName(name);
        habit.setFrequency(frequency);
        habit.setStartDate(startDate);
        habit.setEndDate(endDate);
        habit.setReminder(reminder);

        return habit;
    }
}
