package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.dto.HabitDto;
import io.github.ciamcioo.habit_builder.entity.Habit;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HabitManagementService implements HabitService{
    private static final Logger log = LoggerFactory.getLogger(HabitManagementService.class);

    private static final String HABIT_NOT_PRESENT = "Habit with %s = %s does not exist";

    private final HabitRepository habitRepository;

    public HabitManagementService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Override
    public List<HabitDto> getAllHabits() {
        List<HabitDto> habits = new ArrayList<>();

        habitRepository.findAll()
                .stream()
                .forEach(habit -> {
                    HabitDto habitDto = convertHabitToHabitDto(habit);
                    habits.add(habitDto);
                });

        return Collections.unmodifiableList(habits);
    }

    @Override
    public HabitDto getHabitByName(String name) {
        Habit habit = habitRepository.findHabitByName(name)
                                     .orElseThrow(
                                             () -> new HabitNotPresent(HABIT_NOT_PRESENT, "name", name)
                                     );

        return convertHabitToHabitDto(habit);
    }


    private HabitDto convertHabitToHabitDto(Habit habit) {
        return new HabitDto(habit.getName(),
                            habit.getFrequency(),
                            habit.getStartDate(),
                            habit.getEndDate(),
                            habit.getReminder()
        );
    }


}
