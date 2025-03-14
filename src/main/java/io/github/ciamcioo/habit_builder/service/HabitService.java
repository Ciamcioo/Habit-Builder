package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.HabitDto;
import io.github.ciamcioo.habit_builder.service.aspect.EnableMethodCallLogging;

import java.util.List;

public interface HabitService {

    List<HabitDto> getAllHabits();

    HabitDto getHabitByName(String name);

    String addHabit(HabitDto habit);

    List<String> addHabits(HabitDto... habitDtos);

    HabitDto updateHabit(String habitName, HabitDto updatedHabit);


    void deleteHabit(String name);


}
