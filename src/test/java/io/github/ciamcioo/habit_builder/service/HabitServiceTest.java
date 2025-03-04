package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.commons.HabitBuilder;
import io.github.ciamcioo.habit_builder.commons.HabitFrequency;
import io.github.ciamcioo.habit_builder.dto.HabitDto;
import io.github.ciamcioo.habit_builder.entity.Habit;
import io.github.ciamcioo.habit_builder.exceptions.HabitAlreadyExistsException;
import io.github.ciamcioo.habit_builder.exceptions.HabitNotFoundException;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HabitServiceTest {
    private static final String HABIT_NOT_FOUND_EXCEPTION_MESSAGE = "Habit with name = %s not found";

    HabitService habitService;
    HabitRepository habitRepository;
    HabitDto habitDto;
    Habit habit;
    HabitBuilder builder = HabitBuilder.getInstance();

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        habitService = new HabitManagementService(habitRepository);


        habit = builder.buildHabit();
        habitDto = builder.withTestValues().buildHabitDto();
    }

    @Test
    @DisplayName("Method getAllHabits() should not return null")
    void getAllHabitTest() {
        when(habitRepository.findAll()).thenReturn(new ArrayList<>());

        assertNotNull(habitService.getAllHabits());
    }

    @Test
    @DisplayName("Method getAllHabits() should return instance of List interface")
    void getAllHabitResultInstanceOfList() {
        when(habitRepository.findAll()).thenReturn(new ArrayList<>());

        assertInstanceOf(List.class, habitService.getAllHabits());
    }

    @Test
    @DisplayName("Method getAllHabits() should return empty list if no object where added to it")
    void getAllHabitEmptyListTest() {
        when(habitRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(habitService.getAllHabits().isEmpty());
    }


    @Test
    @DisplayName("Method getAllHabits() should return the list with the same size as the number of habit in the system")
    void getAllHabitCheckSizeTest() {
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit());
        habits.add(new Habit());
        habits.add(new Habit());

        when(habitRepository.findAll()).thenReturn(habits);

        assertEquals(habits.size(), habitService.getAllHabits().size());
    }

    @Test
    @DisplayName("Method getHabitByName() should return HabitDto object")
    void getHabitByNameShouldReturnHabitInstance() {
        when(habitRepository.findHabitByName(anyString())).thenReturn(Optional.of(new Habit()));

        assertInstanceOf(HabitDto.class, habitService.getHabitByName(anyString()));
    }

    @Test
    @DisplayName("Method getHabitByName() should throw HabitNotPresentException with exception message if there is no habit with such a name")
    void getHabitByNameThrowHabitNotPresentException() {
        String invalidName = "invalidName";
        when(habitRepository.findHabitByName(invalidName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HabitNotFoundException.class, () -> habitService.getHabitByName(invalidName));
        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION_MESSAGE, invalidName), exception.getMessage());
    }

    @Test
    @DisplayName("The addHabit() method should return a String object")
    void addHabitShouldReturnHabitName() {
        assertInstanceOf(String.class, habitService.addHabit(habitDto));
    }

    @Test
    @DisplayName("The addHabit() method should return String which name matches the argument habitDto name")
    void addHabitShouldReturnNameOfHabitDto() {
        assertEquals(habitDto.name(), habitService.addHabit(habitDto));
    }

    @Test
    @DisplayName("The addHabit() method should throw HabitAlreadyExistsException which indicates that habit with such a name already exists ")
    void addHabitShouldThrowHabitAlreadyExistsException() {
        when(habitRepository.existsByName(habitDto.name())).thenReturn(true);

        assertThrows(HabitAlreadyExistsException.class, () -> habitService.addHabit(habitDto));
    }

    @Test
    @DisplayName("The addHabits() method shouldn't return null")
    void addHabitsShouldNotReturnNull() {
        assertNotNull(habitService.addHabits(habitDto));
    }

    @Test
    @DisplayName("The addHabits() method should return empty List if no argument is passed")
    void addHabitsShouldReturnEmptyListIfArgumentsDoNotMuch() {
        assertTrue(habitService.addHabits().isEmpty());
    }

    @Test
    @DisplayName("The addHabits() method should return names of added habits")
    void addHabitReturnsListOfAddedHabitsNames() {
        HabitDto habitDto_1 = builder.withName("Test_habit_1").buildHabitDto();
        HabitDto habitDto_2 = builder.withName("Test_habit_2").buildHabitDto();
        HabitDto habitDto_3 = builder.withName("Test_habit_3").buildHabitDto();
        HabitDto habitDto_4 = builder.withName("Test_habit_4").buildHabitDto();

        List<String> expectedResultList = new ArrayList<>();

        expectedResultList.add(habitDto_1.name());
        expectedResultList.add(habitDto_2.name());
        expectedResultList.add(habitDto_3.name());


        when(habitRepository.existsByName(anyString())).thenReturn(false);
        when(habitRepository.existsByName(habitDto_4.name())).thenReturn(true);

        assertEquals(expectedResultList, habitService.addHabits(habitDto_1, habitDto_2, habitDto_3, habitDto_4));
    }

    @Test
    @DisplayName("The updateHabit() method should throw an exception HabitNotFoundException with appropriate message if habit with provided name hasn't been found")
    void updateHabitMethodShouldThrowHabitNotFoundException() {
        String habitName = "test_habit";
        when(habitRepository.findHabitByName(habitName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HabitNotFoundException.class, () -> habitService.updateHabit(habitName, any(HabitDto.class)));
        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION_MESSAGE, habitName), exception.getMessage());
    }

    @Test
    @DisplayName("The updateHabit() method should return HabitDto of update record in database")
    void updateHabitMethodShouldReturnUpdatedRecord() {
        String habitName = "test_habit";

        HabitDto updatedHabit = builder.withTestValues()
                                        .withName("Updated test habit")
                                        .withFrequency(HabitFrequency.MONTHLY)
                                        .buildHabitDto();



        when(habitRepository.findHabitByName(habitName)).thenReturn(Optional.of(habit));


        assertEquals(updatedHabit, habitService.updateHabit(habitName, updatedHabit));
    }

    @Test
    @DisplayName("The updateHabit() method should check if updated habit name doesn't already exists. " +
                 "If it exists method should throw HabitAlreadyExistsException with appropriate message.")
    void updateHabitMethodShouldThrowHabitAlreadyExistsException() {
        String habitName = "test_habit";

        HabitDto updatedHabit = builder.withTestValues()
                                        .withName("already_used_name")
                                        .buildHabitDto();

        when(habitRepository.findHabitByName("test_habit")).thenReturn(Optional.of(habit));
        when(habitRepository.findHabitByName("already_used_name")).thenReturn(Optional.of(new Habit()));

        assertThrows(HabitAlreadyExistsException.class, () -> habitService.updateHabit(habitName, updatedHabit));
    }

    @Test
    @DisplayName("The deletHabit() method should throw HabitNotFoundException with appropriate message if habit with provided habitName doesn't exist")
    void deleteHabitMethodShouldThrowHabitNotFoundException() {
        String habitToDelete = "habitToDelete";
        when(habitRepository.findHabitByName(habitToDelete)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HabitNotFoundException.class, () -> habitService.deleteHabit(habitToDelete));
        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION_MESSAGE, habitToDelete), exception.getMessage());
    }

    @Test
    @DisplayName("The deleteHabit() method should finish without excpetions if the habit was removed")
    void deleteHabitMethodForCorectHabitName() {
        String habitToDelete = "habitToDelete";
        when(habitRepository.findHabitByName(habitToDelete)).thenReturn(Optional.of(new Habit()));

        assertDoesNotThrow(() -> habitService.deleteHabit(habitToDelete));
    }
}
