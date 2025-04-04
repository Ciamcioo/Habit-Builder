package io.github.ciamcioo.habit_builder.service.mapper;

import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import io.github.ciamcioo.habit_builder.model.entity.Habit;
import io.github.ciamcioo.habit_builder.util.HabitBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HabitMapperTest {

    @Autowired
    HabitMapper habitMapper;

    HabitBuilder habitBuilder = HabitBuilder.getInstance();

    @Test
    @DisplayName("If argument is null the return value should be null for toDto() method")
    void toDtoShouldReturnNull() {
        assertNull(habitMapper.toDTO(null));
    }

    @Test
    @DisplayName("If argument is null the return value should be null for toEntity() method")
    void toEntityShouldReturnNull() {
        assertNull(habitMapper.toEntity(null));
    }

    @Test
    @DisplayName("Result habitDTO object should have the same values of fields as argument habit object for toDto() method")
    void toDtoShouldReturnHabitDTOWithTheSameValuesOfFieldsAsArgumentHabit() {
        Habit habit = habitBuilder.withTestValues().buildHabit();

        HabitDTO resultHabitDTO = habitMapper.toDTO(habit);

        assertAll(
                () -> assertEquals(habit.getName(),      resultHabitDTO.name()),
                () -> assertEquals(habit.getFrequency(), resultHabitDTO.frequency()),
                () -> assertEquals(habit.getStartDate(), resultHabitDTO.startDate()),
                () -> assertEquals(habit.getEndDate(),   resultHabitDTO.endDate()),
                () -> assertEquals(habit.getReminder(),  resultHabitDTO.reminder())
        );
    }

    @Test
    @DisplayName("Result Habit object should have the same value of fields as argument habitDTO object after toEntity() method")
    void toEntityShouldReturnHabitObjectWithTheSameValuesOfFieldsAsArgumentHabitDTO() {
        HabitDTO habitDTO = habitBuilder.withTestValues().buildHabitDto();

        Habit resultHabit = habitMapper.toEntity(habitDTO);

        assertAll(
                () -> assertNull(resultHabit.getUuid()),
                () -> assertNull(resultHabit.getUser()),
                () -> assertEquals(habitDTO.name(),      resultHabit.getName()),
                () -> assertEquals(habitDTO.frequency(), resultHabit.getFrequency()),
                () -> assertEquals(habitDTO.startDate(), resultHabit.getStartDate()),
                () -> assertEquals(habitDTO.endDate(),   resultHabit.getEndDate()),
                () -> assertEquals(habitDTO.reminder(),  resultHabit.getReminder())
        );
    }
}
