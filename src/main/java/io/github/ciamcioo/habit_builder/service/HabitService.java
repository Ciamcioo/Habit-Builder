package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import jakarta.json.JsonMergePatch;
import java.util.List;

public interface HabitService {

    List<HabitDTO> getAllHabits();

    HabitDTO getHabitByName(String name);

    String addHabit(HabitDTO habit);

    List<String> addHabits(HabitDTO... habitDTOs);

    HabitDTO updateHabit(String habitName, HabitDTO updatedHabit);

    HabitDTO partialHabitUpdate(String habitName, JsonMergePatch patch);

    void deleteHabit(String name);
}
