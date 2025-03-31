package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import io.github.ciamcioo.habit_builder.util.HabitBuilder;
import io.github.ciamcioo.habit_builder.model.commons.HabitFrequency;
import io.github.ciamcioo.habit_builder.model.entity.Habit;
import io.github.ciamcioo.habit_builder.service.exceptions.HabitAlreadyExistsException;
import io.github.ciamcioo.habit_builder.service.exceptions.HabitNotFoundException;
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
    public static final String HABIT_NOT_FOUND_EXCEPTION_MESSAGE = "Habit with name = testHabit not found";
    public static final String TEST_HABIT_NAME                   = "testHabit";

    HabitService    habitService;
    HabitRepository habitRepository;

    HabitDTO        habitDto;
    Habit           habit;
    HabitBuilder    builder = HabitBuilder.getInstance();

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        habitService = new HabitManagementService(habitRepository);

        habit = builder.withTestValues().buildHabit();
        habitDto = builder.withTestValues().buildHabitDto();
    }

    @Test
    @DisplayName("Method getAllHabits() should not return null")
    void getAllHabitTest() {
        when(habitRepository.findAll()).thenReturn(new ArrayList<>());

        assertNotNull(habitService.getAllHabits());
        verify(habitRepository).findAll();
    }

    @Test
    @DisplayName("Method getAllHabits() should return instance of List interface")
    void getAllHabitResultInstanceOfList() {
        when(habitRepository.findAll()).thenReturn(new ArrayList<>());

        assertInstanceOf(List.class, habitService.getAllHabits());
        verify(habitRepository).findAll();
    }

    @Test
    @DisplayName("Method getAllHabits() should return empty list if no object where added to it")
    void getAllHabitEmptyListTest() {
        when(habitRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(habitService.getAllHabits().isEmpty());
        verify(habitRepository).findAll();
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
        verify(habitRepository).findAll();
    }

    @Test
    @DisplayName("Method getHabitByName() should return HabitDTO object")
    void getHabitByNameShouldReturnHabitInstance() {
        when(habitRepository.findHabitByName(anyString())).thenReturn(Optional.of(new Habit()));

        assertInstanceOf(HabitDTO.class, habitService.getHabitByName(anyString()));
        verify(habitRepository).findHabitByName(anyString());
    }

    @Test
    @DisplayName("Method getHabitByName() should throw HabitNotPresentException with exception message if there is no habit with such a name")
    void getHabitByNameThrowHabitNotPresentException() {
        String invalidName = TEST_HABIT_NAME;

        when(habitRepository.findHabitByName(invalidName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HabitNotFoundException.class, () -> habitService.getHabitByName(invalidName));
        assertEquals(HABIT_NOT_FOUND_EXCEPTION_MESSAGE, exception.getMessage());
        verify(habitRepository).findHabitByName(invalidName);
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
        verify(habitRepository).saveAndFlush(any(Habit.class));
    }

    @Test
    @DisplayName("The addHabit() method should throw HabitAlreadyExistsException which indicates that habit with such a name already exists ")
    void addHabitShouldThrowHabitAlreadyExistsException() {
        when(habitRepository.existsByName(habitDto.name())).thenReturn(true);

        assertThrows(HabitAlreadyExistsException.class, () -> habitService.addHabit(habitDto));
        verify(habitRepository, never()).saveAndFlush(any(Habit.class));
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
    @DisplayName("The addHabits() method should filter out input array from duplications")
    void addHabitsShouldFilterOutInputArray() {
        HabitDTO habitDTO_1 = builder.withName("Test_habit_1").buildHabitDto();
        HabitDTO habitDTO_2 = builder.withName("Test_habit_2").buildHabitDto();
        HabitDTO habitDTO_3 = builder.withName("Test_habit_3").buildHabitDto();
        HabitDTO duplicate  = builder.withName("Test_habit_3").buildHabitDto();

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(habitDTO_1.name());
        expectedResultList.add(habitDTO_2.name());
        expectedResultList.add(habitDTO_3.name());

        when(habitRepository.existsByName(anyString())).thenReturn(false);

        List<String> resultList = habitService.addHabits(habitDTO_1, habitDTO_2, habitDTO_3, duplicate);

        for (String result : resultList) {
            assertTrue(expectedResultList.contains(result));
        }
        verify(habitRepository).saveAllAndFlush(anyList());
    }

    @Test
    @DisplayName("The addHabits() method should return names of added habits")
    void addHabitReturnsListOfAddedHabitsNames() {
        HabitDTO habitDTO_1 = builder.withName("Test_habit_1").buildHabitDto();
        HabitDTO habitDTO_2 = builder.withName("Test_habit_2").buildHabitDto();
        HabitDTO habitDTO_3 = builder.withName("Test_habit_3").buildHabitDto();
        HabitDTO habitDTO_4 = builder.withName("Test_habit_4").buildHabitDto();

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(habitDTO_1.name());
        expectedResultList.add(habitDTO_2.name());
        expectedResultList.add(habitDTO_3.name());

        when(habitRepository.existsByName(anyString())).thenReturn(false);
        when(habitRepository.existsByName(habitDTO_4.name())).thenReturn(true);

        List<String> resultList = habitService.addHabits(habitDTO_1, habitDTO_2, habitDTO_3, habitDTO_4);

        for (String result : resultList) {
            assertTrue(expectedResultList.contains(result));
        }
        verify(habitRepository).saveAllAndFlush(anyList());
    }

    @Test
    @DisplayName("The updateHabit() method should throw an exception HabitNotFoundException with appropriate message if habit with provided name hasn't been found")
    void updateHabitMethodShouldThrowHabitNotFoundException() {
        String habitToUpdateName = TEST_HABIT_NAME;

        when(habitRepository.findHabitByName(habitToUpdateName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HabitNotFoundException.class, () -> habitService.updateHabit(habitToUpdateName, any(HabitDTO.class)));
        assertEquals(HABIT_NOT_FOUND_EXCEPTION_MESSAGE, exception.getMessage());
        verify(habitRepository, never()).saveAndFlush(any(Habit.class));
    }

    @Test
    @DisplayName("The updateHabit() method should return HabitDTO of update record in database")
    void updateHabitMethodShouldReturnUpdatedRecord() {
        String habitToUpdateName = TEST_HABIT_NAME;
        HabitDTO updatedHabitDTO = builder.withTestValues()
                                       .withName("Updated test habit")
                                       .withFrequency(HabitFrequency.MONTHLY)
                                       .buildHabitDto();

        when(habitRepository.findHabitByName(habitToUpdateName)).thenReturn(Optional.of(habit));

        assertEquals(updatedHabitDTO, habitService.updateHabit(habitToUpdateName, updatedHabitDTO));
        verify(habitRepository).saveAndFlush(habit);
    }

    @Test
    @DisplayName("The updateHabit() method should check if updated habit name doesn't already exists. " +
                 "If it exists method should throw HabitAlreadyExistsException with appropriate message.")
    void updateHabitMethodShouldThrowHabitAlreadyExistsException() {
        String habitName = TEST_HABIT_NAME;
        String updatedHabitName = "updatedHabitName";
        HabitDTO updatedHabit = builder.withTestValues()
                                       .withName(updatedHabitName)
                                       .buildHabitDto();

        when(habitRepository.findHabitByName(habitName)).thenReturn(Optional.of(new Habit()));
        when(habitRepository.findHabitByName(updatedHabitName)).thenReturn(Optional.of(new Habit()));

        assertThrows(HabitAlreadyExistsException.class, () -> habitService.updateHabit(habitName, updatedHabit));
        verify(habitRepository, never()).saveAndFlush(any(Habit.class));
    }

    @Test
    @DisplayName("The deleteHabit() method should throw HabitNotFoundException with appropriate message if habit with provided habitName doesn't exist")
    void deleteHabitMethodShouldThrowHabitNotFoundException() {
        String habitToDelete = TEST_HABIT_NAME;

        when(habitRepository.findHabitByName(habitToDelete)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HabitNotFoundException.class, () -> habitService.deleteHabit(habitToDelete));
        assertEquals(HABIT_NOT_FOUND_EXCEPTION_MESSAGE, exception.getMessage());
        verify(habitRepository, never()).delete(any(Habit.class));
    }

    @Test
    @DisplayName("The deleteHabit() method should finish without exceptions if the habit was removed")
    void deleteHabitMethodForCorrectHabitName() {
        Habit habitEntityToDelete = builder.buildHabit();

        when(habitRepository.findHabitByName(habitEntityToDelete.getName())).thenReturn(Optional.of(habitEntityToDelete));

        assertDoesNotThrow(() -> habitService.deleteHabit(habitEntityToDelete.getName()));

        verify(habitRepository).delete(habitEntityToDelete);
        verify(habitRepository).flush();
    }
}
