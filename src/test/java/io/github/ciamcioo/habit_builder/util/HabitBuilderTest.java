package io.github.ciamcioo.habit_builder.util;

import io.github.ciamcioo.habit_builder.model.commons.HabitFrequency;
import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;

import java.time.LocalDate;
import java.util.UUID;

import io.github.ciamcioo.habit_builder.model.entity.Habit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HabitBuilderTest {

    public static final String         testName      = "Foo_habit";
    public static final HabitFrequency testFrequency = HabitFrequency.DAILY;
    public static final LocalDate      testStartDate = LocalDate.now().minusDays(1);
    public static final LocalDate      testEndDate   = LocalDate.now().plusYears(1);

    HabitBuilder builder;


    @BeforeEach
    void setup() {
        builder = HabitBuilder.getInstance();
    }

    @Test
    void testBuilderHabitInputData_HabitDataShouldBeTheSameAsBuilderOne() {

        String         name      = "Habit name";
        HabitFrequency frequency = HabitFrequency.MONTHLY;
        LocalDate      startDate = LocalDate.now().minusDays(4);
        LocalDate      endDate   = LocalDate.now().plusDays(5);
        Boolean        reminder  = false;

        builder = builder.withName(name)
                .withFrequency(frequency)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withReminder(reminder);

        HabitDTO builtHabit = builder.buildHabitDto();

        assertAll(
                () -> assertEquals(name, builtHabit.name()),
                () -> assertEquals(frequency, builtHabit.frequency()),
                () -> assertEquals(startDate, builtHabit.startDate()),
                () -> assertEquals(endDate, builtHabit.endDate()),
                () -> assertEquals(reminder, builtHabit.reminder())
        );
    }

    @Test
    void testBuilderHabitWithDefaultValues_BuildObjectShouldHaveTheSameValues() {
        HabitDTO builtHabit = builder.withTestValues().buildHabitDto();

        assertAll(
                () -> assertEquals(testName, builtHabit.name()),
                () -> assertEquals(testFrequency, builtHabit.frequency()),
                () -> assertEquals(testStartDate, builtHabit.startDate()),
                () -> assertEquals(LocalDate.now().plusYears(1), builtHabit.endDate()),
                () -> assertEquals(true, builtHabit.reminder())
        );
    }

    @Test
    @DisplayName("The buildHabit() method should return a Habit object with fields equals to set values.")
    void testBuildHabitForSetValues() {

        UUID           uuid      = UUID.randomUUID();
        String         name      = "Habit name";
        HabitFrequency frequency = HabitFrequency.MONTHLY;
        LocalDate      startDate = LocalDate.now().minusDays(4);
        LocalDate      endDate   = LocalDate.now().plusDays(5);
        Boolean        reminder  = false;

        builder.withUUID(uuid)
                .withName(name)
                .withFrequency(frequency)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withReminder(reminder);

        Habit builtHabit = builder.buildHabit();

        assertAll(
                () -> assertEquals(uuid, builtHabit.getUuid()),
                () -> assertEquals(name, builtHabit.getName()),
                () -> assertEquals(frequency, builtHabit.getFrequency()),
                () -> assertEquals(startDate, builtHabit.getStartDate()),
                () -> assertEquals(endDate, builtHabit.getEndDate()),
                () -> assertEquals(reminder, builtHabit.getReminder())
        );
    }

    @Test
    @DisplayName("The buildHabit() method should return a Habit object with fields set to their default values")
    void testBuildHabitWithDefaultValues() {
        Habit builtHabit = builder.withTestValues().buildHabit();

        assertAll(
                () -> assertInstanceOf(UUID.class, builtHabit.getUuid()),
                () -> assertEquals(testName, builtHabit.getName()),
                () -> assertEquals(testFrequency, builtHabit.getFrequency()),
                () -> assertEquals(testStartDate, builtHabit.getStartDate()),
                () -> assertEquals(testEndDate, builtHabit.getEndDate()),
                () -> assertEquals(true, builtHabit.getReminder())
        );
    }



}