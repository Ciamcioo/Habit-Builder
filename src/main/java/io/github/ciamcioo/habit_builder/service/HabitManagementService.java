package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import io.github.ciamcioo.habit_builder.model.entity.Habit;
import io.github.ciamcioo.habit_builder.service.aspect.EnableExceptionLogging;
import io.github.ciamcioo.habit_builder.service.aspect.EnableMethodCallLogging;
import io.github.ciamcioo.habit_builder.service.aspect.EnableMethodLogging;
import io.github.ciamcioo.habit_builder.service.exceptions.ConversionException;
import io.github.ciamcioo.habit_builder.service.exceptions.HabitAlreadyExistsException;
import io.github.ciamcioo.habit_builder.service.exceptions.HabitNotFoundException;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;

import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HabitManagementService implements HabitService{
    private static final String HABIT_NOT_FOUND     = "Habit with name = %s not found";
    private static final String HABIT_ALREADY_EXIST = "Habit with name = %s already exists in database";

    private final HabitRepository habitRepository;

    public HabitManagementService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Override
    @EnableMethodLogging
    public List<HabitDTO> getAllHabits() {
        List<HabitDTO> habits = new ArrayList<>();
        habitRepository.findAll()
                        .forEach(habit -> {
                            HabitDTO habitDto = convertHabitToHabitDto(habit);
                            habits.add(habitDto);
                        });

        return Collections.unmodifiableList(habits);
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public HabitDTO getHabitByName(String name) {
        Habit habit = habitRepository.findHabitByName(name)
                                     .orElseThrow(
                                             () -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND, name))
                                     );

        return convertHabitToHabitDto(habit);
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public String addHabit(HabitDTO habit) {
        if (habitRepository.existsByName(habit.name())) {
            throw new HabitAlreadyExistsException(String.format(HABIT_ALREADY_EXIST, habit.name()));
        }

        Habit record = new Habit(habit.name(),
                                habit.frequency(),
                                habit.startDate(),
                                habit.endDate(),
                                habit.reminder()
        );

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
                                                  .map(this::convertHabitDtoToHabit)
                                                  .toList();

        habitRepository.saveAllAndFlush(habitsToSave);

        return habitsToSave.stream().map(Habit::getName).toList();
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public HabitDTO updateHabit(String habitName, HabitDTO updatedHabit) {

        Habit record = habitRepository.findHabitByName(habitName)
                                      .orElseThrow(() -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND, habitName)));

        Optional<Habit> recordWithUpdateName = habitRepository.findHabitByName(updatedHabit.name());
        if (recordWithUpdateName.isPresent()) {
            throw new HabitAlreadyExistsException(String.format(HABIT_ALREADY_EXIST, updatedHabit.name()));
        }

        record.setName(updatedHabit.name());
        record.setFrequency(updatedHabit.frequency());
        record.setStartDate(updatedHabit.startDate());
        record.setEndDate(updatedHabit.endDate());
        record.setReminder(updatedHabit.reminder());

        habitRepository.saveAndFlush(record);

        return updatedHabit;
    }

    @Override
    @EnableMethodCallLogging
    @EnableExceptionLogging
    public void deleteHabit(String habitName) {
        Habit habit = habitRepository.findHabitByName(habitName)
                                     .orElseThrow(() -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND, habitName)));

        habitRepository.delete(habit);
        habitRepository.flush();
    }

    private HabitDTO convertHabitToHabitDto(Habit habit) throws ConversionException {
        try {
            return new HabitDTO(habit.getName(),
                    habit.getFrequency(),
                    habit.getStartDate(),
                    habit.getEndDate(),
                    habit.getReminder()
            );
        } catch (RuntimeException e) {
            throw new ConversionException(Habit.class.getSimpleName(), HabitDTO.class.getSimpleName());
        }
    }

    private Habit convertHabitDtoToHabit(HabitDTO habitDto) throws ConversionException {
        try {
            return new Habit(habitDto.name(),
                    habitDto.frequency(),
                    habitDto.startDate(),
                    habitDto.endDate(),
                    habitDto.reminder()
            );
        } catch (RuntimeException e) {
            throw new ConversionException(HabitDTO.class.getSimpleName(), Habit.class.getSimpleName());
        }
    }
}
