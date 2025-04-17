package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import io.github.ciamcioo.habit_builder.model.entity.Habit;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableExceptionLogging;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodCallLogging;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodLogging;
import io.github.ciamcioo.habit_builder.exception.HabitAlreadyExistsException;
import io.github.ciamcioo.habit_builder.exception.HabitNotFoundException;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;

import io.github.ciamcioo.habit_builder.service.mapper.HabitMapper;
import jakarta.json.JsonMergePatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HabitManagementService implements HabitService{
    private static final String HABIT_NOT_FOUND_MESSAGE_FORMAT     = "Habit with given name: %s not found";
    private static final String HABIT_ALREADY_EXIST_MESSAGE_FORMAT = "Habit with given name: %s already exists in database";

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;
    private final MergePatchHelper mergePatchHelper;

    @Autowired
    public HabitManagementService(HabitRepository habitRepository, HabitMapper habitMapper, MergePatchHelper mergePatchHelper) {
        this.habitRepository = habitRepository;
        this.habitMapper = habitMapper;
        this.mergePatchHelper = mergePatchHelper;
    }

    @Override
    @EnableMethodLogging
    public List<HabitDTO> getAllHabits() {
        List<Habit> persistedHabits = habitRepository.findAll();

        return persistedHabits.stream()
                              .map(habitMapper::toDTO)
                              .toList();
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public HabitDTO getHabitByName(String name) {
        Habit habit = habitRepository.findHabitByName(name)
                                     .orElseThrow(
                                             () -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND_MESSAGE_FORMAT, name))
                                     );

        return habitMapper.toDTO(habit) ;
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public String addHabit(HabitDTO habit) {
        if (habitRepository.existsByName(habit.name())) {
            throw new HabitAlreadyExistsException(String.format(HABIT_ALREADY_EXIST_MESSAGE_FORMAT, habit.name()));
        }

        Habit record = habitMapper.toEntity(habit);
        habitRepository.saveAndFlush(record);

        return habit.name();
    }

    @Override
    @EnableMethodLogging
    public List<String> addHabits(HabitDTO... habitDTOs) {
        Set<HabitDTO> uniqueHabitDTOs = new HashSet<>(List.of(habitDTOs));

        List<Habit> habitsToSave = uniqueHabitDTOs.stream()
                                                  .filter(habitDto ->
                                                      !habitRepository.existsByName(habitDto.name())
                                                  )
                                                  .map(habitMapper::toEntity)
                                                  .toList();

        habitRepository.saveAllAndFlush(habitsToSave);

        return habitsToSave.stream().map(Habit::getName).toList();
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public HabitDTO updateHabit(String habitName, HabitDTO updatedHabit) {
        Optional<Habit> recordOpt = habitRepository.findHabitByName(habitName);

        if (recordOpt.isEmpty()) {
            addHabit(updatedHabit);
            return updatedHabit;
        }

        Habit record = recordOpt.get();

        habitRepository.findHabitByName(updatedHabit.name())
                       .ifPresent(ex -> {
                           throw new HabitAlreadyExistsException();
                       });

        record.setName(updatedHabit.name());
        record.setFrequency(updatedHabit.frequency());
        record.setStartDate(updatedHabit.startDate());
        record.setEndDate(updatedHabit.endDate());
        record.setReminder(updatedHabit.reminder());

        habitRepository.saveAndFlush(record);

        return updatedHabit;
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public HabitDTO partialHabitUpdate(String habitName, JsonMergePatch fieldsToUpdate) {
        Habit habit = habitRepository.findHabitByName(habitName)
                                     .orElseThrow(HabitNotFoundException::new);

        habit = mergePatchHelper.mergePatch(fieldsToUpdate, habit, Habit.class);

        return habitMapper.toDTO(habit);
    }

    @Override
    @EnableMethodCallLogging
    @EnableExceptionLogging
    public void deleteHabit(String habitName) {
        Habit habit = habitRepository.findHabitByName(habitName)
                                     .orElseThrow(() -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND_MESSAGE_FORMAT, habitName)));

        habitRepository.delete(habit);
        habitRepository.flush();
    }
}
