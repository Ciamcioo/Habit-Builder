package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import io.github.ciamcioo.habit_builder.service.mapper.HabitMapper;
import io.github.ciamcioo.habit_builder.util.HabitBuilder;
import io.github.ciamcioo.habit_builder.model.commons.HabitFrequency;
import io.github.ciamcioo.habit_builder.model.entity.Habit;
import io.github.ciamcioo.habit_builder.exception.HabitAlreadyExistsException;
import io.github.ciamcioo.habit_builder.exception.HabitNotFoundException;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;

import jakarta.json.Json;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HabitServiceTest {
    // CONSTANT
    public static final String HABIT_NOT_FOUND_EXCEPTION_MESSAGE = "Habit with given name: testHabit not found";
    public static final String TEST_HABIT_NAME                   = "testHabit";

    // TESTED SERVICE
    private static HabitService    habitService;

    // MOCK SERVICE
    private static HabitRepository  habitRepository;
    private static HabitMapper      habitMapper;
    private static MergePatchHelper mergePatchHelper;

    // HELPER OBJECTS
    private static HabitBuilder habitBuilder = HabitBuilder.getInstance();
    private static Habit        habit;
    private static HabitDTO     habitDto;

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        habitMapper = mock(HabitMapper.class);
        mergePatchHelper = mock(MergePatchHelper.class);
        habitService = Mockito.spy(new HabitManagementService(habitRepository, habitMapper, mergePatchHelper));

        habitBuilder = habitBuilder.withTestValues();
        habit = habitBuilder.buildHabit();
        habitDto = habitBuilder.buildHabitDto();
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
        when(habitMapper.toDTO(habit)).thenReturn(habitDto);
        when(habitRepository.findHabitByName(anyString())).thenReturn(Optional.of(habit));

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
        when(habitMapper.toEntity(habitDto)).thenReturn(habit);

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
        when(habitMapper.toEntity(habitDto)).thenReturn(habit);

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
        HabitDTO habitDTO_1 = habitBuilder.withName("Test_habit_1").buildHabitDto();
        Habit habitEntity_1 = habitBuilder.buildHabit();
        HabitDTO habitDTO_2 = habitBuilder.withName("Test_habit_2").buildHabitDto();
        Habit habitEntity_2 = habitBuilder.buildHabit();
        HabitDTO habitDTO_3 = habitBuilder.withName("Test_habit_3").buildHabitDto();
        Habit habitEntity_3 = habitBuilder.buildHabit();
        HabitDTO duplicate  = habitBuilder.withName("Test_habit_3").buildHabitDto();

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(habitDTO_1.name());
        expectedResultList.add(habitDTO_2.name());
        expectedResultList.add(habitDTO_3.name());

        when(habitMapper.toEntity(habitDTO_1)).thenReturn(habitEntity_1);
        when(habitMapper.toEntity(habitDTO_2)).thenReturn(habitEntity_2);
        when(habitMapper.toEntity(habitDTO_3)).thenReturn(habitEntity_3);
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
        HabitDTO habitDTO_1 = habitBuilder.withName("Test_habit_1").buildHabitDto();
        Habit habitEntity_1 = habitBuilder.buildHabit();
        HabitDTO habitDTO_2 = habitBuilder.withName("Test_habit_2").buildHabitDto();
        Habit habitEntity_2 = habitBuilder.buildHabit();
        HabitDTO habitDTO_3 = habitBuilder.withName("Test_habit_3").buildHabitDto();
        Habit habitEntity_3 = habitBuilder.buildHabit();
        HabitDTO habitDTO_4 = habitBuilder.withName("Test_habit_4").buildHabitDto();

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(habitDTO_1.name());
        expectedResultList.add(habitDTO_2.name());
        expectedResultList.add(habitDTO_3.name());

        when(habitRepository.existsByName(anyString())).thenReturn(false);
        when(habitRepository.existsByName(habitDTO_4.name())).thenReturn(true);
        when(habitMapper.toEntity(habitDTO_1)).thenReturn(habitEntity_1);
        when(habitMapper.toEntity(habitDTO_2)).thenReturn(habitEntity_2);
        when(habitMapper.toEntity(habitDTO_3)).thenReturn(habitEntity_3);

        List<String> resultList = habitService.addHabits(habitDTO_1, habitDTO_2, habitDTO_3, habitDTO_4);

        for (String result : resultList) {
            assertTrue(expectedResultList.contains(result));
        }

        verify(habitRepository).saveAllAndFlush(anyList());
    }

    @Test
    @DisplayName("The updateHabit() method should call addHabit method if there is not habit with specified habit name argument")
    void updateHabitMethodShouldThrowHabitNotFoundException() {
        String habitToUpdateName = TEST_HABIT_NAME;

        when(habitRepository.findHabitByName(habitToUpdateName)).thenReturn(Optional.empty());
        when(habitMapper.toEntity(habitDto)).thenReturn(habit);

        habitService.updateHabit(habitToUpdateName, habitDto);

        verify(habitService).addHabit(habitDto);
        verify(habitRepository).saveAndFlush(any(Habit.class));
    }

    @Test
    @DisplayName("The updateHabit() method should return HabitDTO of update record in database")
    void updateHabitMethodShouldReturnUpdatedRecord() {
        String habitToUpdateName = TEST_HABIT_NAME;
        HabitDTO updatedHabitDTO = habitBuilder.withTestValues()
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
        HabitDTO updatedHabit = habitBuilder.withTestValues()
                                            .withName(updatedHabitName)
                                            .buildHabitDto();

        when(habitRepository.findHabitByName(habitName)).thenReturn(Optional.of(new Habit()));
        when(habitRepository.findHabitByName(updatedHabitName)).thenReturn(Optional.of(new Habit()));

        assertThrows(HabitAlreadyExistsException.class, () -> habitService.updateHabit(habitName, updatedHabit));

        verify(habitRepository, never()).saveAndFlush(any(Habit.class));
    }

    @Test
    @DisplayName("Partial update in updatePartialHabit() method should not return null")
    void partialHabitUpdateShouldNotReturnNullValue() {
        JsonObject json = Json.createObjectBuilder().build();
        JsonMergePatch mergePatch = Json.createMergePatch(json);

        when(habitRepository.findHabitByName(TEST_HABIT_NAME)).thenReturn(Optional.of(habit));
        when(habitMapper.toDTO(habit)).thenReturn(habitDto);


        assertNotNull(habitService.partialHabitUpdate(TEST_HABIT_NAME, mergePatch));
    }

    @Test
    @DisplayName("Partial update in updatePartialHabit() method should return habit with the same name as argument name")
    void partialHabitUpdateShouldReturnHabitWithMatchingArgumentName() {
        String habitNameToUpdate = habit.getName();
        JsonObject json = Json.createObjectBuilder().build();
        JsonMergePatch mergePatch = Json.createMergePatch(json);

        when(habitRepository.findHabitByName(habitNameToUpdate)).thenReturn(Optional.of(habit));
        when(habitMapper.toDTO(habit)).thenReturn(habitDto);

        HabitDTO resultHabitDTO = habitService.partialHabitUpdate(habitNameToUpdate, mergePatch);

        assertEquals(habitNameToUpdate, resultHabitDTO.name());
    }

    @Test
    @DisplayName("Partial update should make a call to habit repository")
    void partialHabitUpdateShouldMakeACallToHabitRepository() {
        JsonObject json = Json.createObjectBuilder().build();
        JsonMergePatch mergePatch = Json.createMergePatch(json);

        when(habitRepository.findHabitByName(TEST_HABIT_NAME)).thenReturn(Optional.of(habit));

        habitService.partialHabitUpdate(TEST_HABIT_NAME, mergePatch);

        verify(habitRepository).findHabitByName(TEST_HABIT_NAME);
    }

    @Test
    @DisplayName("Partial update should throw HabitNotFoundException if habit with specified name hasn't been found")
    void partialHabitUpdateShouldThrowHabitNotFoundException() {
        JsonObject json = Json.createObjectBuilder().build();
        JsonMergePatch mergePatch = Json.createMergePatch(json);
        String notPersistedHabitName = TEST_HABIT_NAME;

        when(habitRepository.findHabitByName(notPersistedHabitName)).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.partialHabitUpdate(notPersistedHabitName, mergePatch));
    }

    @Test
    @DisplayName("Partial update should return habitDTO with updated fields")
    void partialHabitUpdateShouldReturnTheHabitDTOWithUpdateFields() {
        String habitNameToUpdate = habit.getName();
        JsonObject fieldsToUpdate = Json.createObjectBuilder()
                                        .add("name", "New habit name")
                                        .add("frequency", String.valueOf(HabitFrequency.WEEKLY))
                                        .add("startDate", String.valueOf(LocalDate.of(1990, Month.APRIL, 1)))
                                        .add("endDate", String.valueOf(LocalDate.of(1991, Month.APRIL ,1)))
                                        .add("reminder", false)
                                        .build();
        JsonMergePatch updatePatch = Json.createMergePatch(fieldsToUpdate);
        Habit updatedHabit = habitBuilder.withUUID(habit.getUuid())
                                         .withName("New habit name")
                                         .withFrequency(HabitFrequency.WEEKLY)
                                         .withStartDate(LocalDate.of(1990, Month.APRIL, 1))
                                         .withEndDate(LocalDate.of(1991, Month.APRIL, 1))
                                         .withReminder(false)
                                         .buildHabit();
        HabitDTO updateHabitDTO = habitBuilder.buildHabitDto();

        when(habitRepository.findHabitByName(habitNameToUpdate)).thenReturn(Optional.of(habit));
        when(mergePatchHelper.mergePatch(updatePatch, habit, Habit.class)).thenReturn(updatedHabit);
        when(habitMapper.toDTO(habit)).thenReturn(updateHabitDTO);

        HabitDTO resultHabitDTO = habitService.partialHabitUpdate(habitNameToUpdate, updatePatch);

        assertAll(
                () -> assertEquals(fieldsToUpdate.getString("name"),      resultHabitDTO.name()),
                () -> assertEquals(fieldsToUpdate.getString("frequency"), resultHabitDTO.frequency().toString()),
                () -> assertEquals(fieldsToUpdate.getString("startDate"), resultHabitDTO.startDate().toString()),
                () -> assertEquals(fieldsToUpdate.getString("endDate"),   resultHabitDTO.endDate().toString()),
                () -> assertEquals(fieldsToUpdate.getBoolean("reminder"), resultHabitDTO.reminder())
        );
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
        Habit habitEntityToDelete = habit;

        when(habitRepository.findHabitByName(habitEntityToDelete.getName())).thenReturn(Optional.of(habitEntityToDelete));

        assertDoesNotThrow(() -> habitService.deleteHabit(habitEntityToDelete.getName()));

        verify(habitRepository).delete(habitEntityToDelete);
        verify(habitRepository).flush();
    }
}
