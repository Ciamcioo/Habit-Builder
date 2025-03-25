package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.service.exceptions.ConversionException;
import io.github.ciamcioo.habit_builder.util.HabitBuilder;
import io.github.ciamcioo.habit_builder.model.dto.HabitDto;
import io.github.ciamcioo.habit_builder.model.entity.Habit;

import io.github.ciamcioo.habit_builder.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class HabitManagementServiceTest {
    public static final String CONVERSION_EXCEPTION_FROM_HABIT_DTO_TO_HABIT_MESSAGE  = "Conversion of HabitDTO to Habit ended up with failure!";
    public static final String CONVERSION_EXCEPTION_FROM_HABIT_TO_HABIT_DTO_MESSAGE  = "Conversion of Habit to HabitDTO ended up with failure!";

    private HabitManagementService habitManagementService;
    private HabitRepository habitRepository;
    private Habit habit;
    private HabitDto habitDto;

    @BeforeEach
    void setup() {
        HabitRepository habitRepository = mock(HabitRepository.class);
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
    @DisplayName("Method convertHabitToHabitDTO() should throw ConversionException with suitable message")
    void convertHabitToHabitDTOShouldThrowConversionException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertHabitToHabitDto = HabitManagementService.class.getDeclaredMethod("convertHabitToHabitDto", Habit.class);
        convertHabitToHabitDto.setAccessible(true);

        Habit testHabit = null;

        Exception exception = assertThrows(InvocationTargetException.class, () ->  convertHabitToHabitDto.invoke(habitManagementService, testHabit));
        assertInstanceOf(ConversionException.class, exception.getCause());
        assertEquals(CONVERSION_EXCEPTION_FROM_HABIT_TO_HABIT_DTO_MESSAGE,
                     exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Method convertHabitDtoToHabit() should return Habit object wiht the same values as the HabitDTO has")
    void convertHabitDtoToHabitTest() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method convertHabitDtoToHabit = HabitManagementService.class.getDeclaredMethod("convertHabitDtoToHabit", HabitDTO.class);
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

    @Test
    @DisplayName("Method convertHabitDtoToHabit() should throw ConversionException with sutiable message")
    void convertHabitDtoToHabitShouldThrowConversionException() throws NoSuchMethodException {
        Method convertHabitDtoToHabit = HabitManagementService.class.getDeclaredMethod("convertHabitDtoToHabit", HabitDTO.class);
        convertHabitDtoToHabit.setAccessible(true);

        HabitDTO testHabit = null;

        Exception exception = assertThrows(InvocationTargetException.class, () ->  convertHabitDtoToHabit.invoke(habitManagementService, testHabit));
        assertInstanceOf(ConversionException.class, exception.getCause());

        assertEquals(CONVERSION_EXCEPTION_FROM_HABIT_DTO_TO_HABIT_MESSAGE,
                     exception.getCause().getMessage());
    }





}
