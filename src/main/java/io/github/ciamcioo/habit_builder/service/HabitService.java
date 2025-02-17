package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.dto.HabitDto;
import io.github.ciamcioo.habit_builder.entity.Habit;

import java.util.List;
import java.util.UUID;

public interface HabitService {

    List<HabitDto> getAllHabits();

    HabitDto getHabitByName(String name);



}
