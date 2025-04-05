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
    // CONSTANT
    public static final String         DEF_NAME       = "Foo_habit";
    public static final HabitFrequency DEF_FREQUENCY  = HabitFrequency.DAILY;
    public static final LocalDate      DEF_START_TIME = LocalDate.now().minusDays(1);
    public static final LocalDate      DEF_END_TIME   = LocalDate.now().plusYears(1);
    public static final Boolean        DEF_REMINDER   = true;

    public static final UUID           ID         = UUID.randomUUID();
    public static final String         NAME       = "Habit name";
    public static final HabitFrequency FREQUENCY  = HabitFrequency.MONTHLY;
    public static final LocalDate      START_DATE = LocalDate.now().minusDays(4);
    public static final LocalDate      END_DATE   = LocalDate.now().plusDays(5);
    public static final Boolean        REMINDER   = false;

    // TESTED SERVICE
    HabitBuilder habitBuilder;


    @BeforeEach
    void setup() {
        habitBuilder = HabitBuilder.getInstance();
    }

    @Test
    @DisplayName("Build object should have the same values as defined fields")
    void buildObjectShouldHaveTheSameValuesAsDefinedFields() {
        HabitDTO builtHabitDto = habitBuilder.withName(NAME)
                                             .withFrequency(FREQUENCY)
                                             .withStartDate(START_DATE)
                                             .withEndDate(END_DATE)
                                             .withReminder(REMINDER)
                                             .buildHabitDto();

        assertAll(
                () -> assertEquals(NAME,       builtHabitDto.name()),
                () -> assertEquals(FREQUENCY,  builtHabitDto.frequency()),
                () -> assertEquals(START_DATE, builtHabitDto.startDate()),
                () -> assertEquals(END_DATE,   builtHabitDto.endDate()),
                () -> assertEquals(REMINDER,   builtHabitDto.reminder())
        );
    }

    @Test
    @DisplayName("Built Habit object should have fields set to default values.")
    void buildHabitShouldHaveFieldsSetToDefaultValues() {
        HabitDTO builtHabitDto = habitBuilder.withTestValues().buildHabitDto();

        assertAll(
                () -> assertEquals(DEF_NAME,       builtHabitDto.name()),
                () -> assertEquals(DEF_FREQUENCY,  builtHabitDto.frequency()),
                () -> assertEquals(DEF_START_TIME, builtHabitDto.startDate()),
                () -> assertEquals(DEF_END_TIME,   builtHabitDto.endDate()),
                () -> assertEquals(DEF_REMINDER,   builtHabitDto.reminder())
        );
    }

    @Test
    @DisplayName("The buildHabit() method should return a Habit object with fields equals to set values.")
    void testBuildHabitForSetValues() {
        Habit builtHabit = habitBuilder.withUUID(ID)
                                       .withName(NAME)
                                       .withFrequency(FREQUENCY)
                                       .withStartDate(START_DATE)
                                       .withEndDate(END_DATE)
                                       .withReminder(REMINDER)
                                       .buildHabit();

        assertAll(
                () -> assertEquals(ID,         builtHabit.getUuid()),
                () -> assertEquals(NAME,       builtHabit.getName()),
                () -> assertEquals(FREQUENCY,  builtHabit.getFrequency()),
                () -> assertEquals(START_DATE, builtHabit.getStartDate()),
                () -> assertEquals(END_DATE,   builtHabit.getEndDate()),
                () -> assertEquals(REMINDER,   builtHabit.getReminder())
        );
    }

    @Test
    @DisplayName("The buildHabit() method should return a Habit object with fields set to their default values")
    void testBuildHabitWithDefaultValues() {
        Habit builtHabit = habitBuilder.withTestValues().buildHabit();

        assertAll(
                () -> assertInstanceOf(UUID.class, builtHabit.getUuid()),
                () -> assertEquals(DEF_NAME,       builtHabit.getName()),
                () -> assertEquals(DEF_FREQUENCY,  builtHabit.getFrequency()),
                () -> assertEquals(DEF_START_TIME, builtHabit.getStartDate()),
                () -> assertEquals(DEF_END_TIME,   builtHabit.getEndDate()),
                () -> assertEquals(DEF_REMINDER,   builtHabit.getReminder())
        );
    }



}