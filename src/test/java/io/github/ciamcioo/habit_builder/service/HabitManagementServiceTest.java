package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.util.HabitBuilder;
import io.github.ciamcioo.habit_builder.model.dto.HabitDto;
import io.github.ciamcioo.habit_builder.model.entity.Habit;

import io.github.ciamcioo.habit_builder.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class HabitManagementServiceTest {
    private HabitManagementService habitManagementService;
    private HabitRepository habitRepository;
    private Habit habit;
    private HabitDto habitDto;

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        habitManagementService = new HabitManagementService(habitRepository);

        habit = HabitBuilder.getInstance().withTestValues().buildHabit();
        habitDto = HabitBuilder.getInstance().withTestValues().buildHabitDto();
    }

    @Test
    @DisplayName("Method convertHabitToHabitDto() should return the same habitDto with the same values as the Habit object contains")
    void convertHabitToHabitDtoEqualityCheck() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertHabitToHabitDto = HabitManagementService.class.getDeclaredMethod("convertHabitToHabitDto", Habit.class);
        convertHabitToHabitDto.setAccessible(true);

        HabitDto habitDto = (HabitDto) convertHabitToHabitDto.invoke(habitManagementService, habit);

        assertAll(
                () -> assertEquals(habit.getName(), habitDto.name()),
                () -> assertEquals(habit.getFrequency(), habitDto.frequency()),
                () -> assertEquals(habit.getStartDate(), habitDto.startDate()),
                () -> assertEquals(habit.getEndDate(), habitDto.endDate()),
                () -> assertEquals(habit.getReminder(), habitDto.reminder())
        );
    }

    @Test
    @DisplayName("Method convertHabitDtoToHabit() should return Habit object wiht the same values as the HabitDto has")
    void convertHabitDtoToHabitTest() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method convertHabitDtoToHabit = HabitManagementService.class.getDeclaredMethod("convertHabitDtoToHabit", HabitDto.class);
        convertHabitDtoToHabit.setAccessible(true);

        Habit habit = (Habit) convertHabitDtoToHabit.invoke(habitManagementService, habitDto);

        assertAll(
                () -> assertEquals(habitDto.name(),habit.getName() ),
                () -> assertEquals(habitDto.frequency(),habit.getFrequency()),
                () -> assertEquals(habitDto.startDate(),habit.getStartDate()),
                () -> assertEquals(habitDto.endDate(), habit.getEndDate()),
                () -> assertEquals(habitDto.reminder(), habit.getReminder())
        );
    }




}
