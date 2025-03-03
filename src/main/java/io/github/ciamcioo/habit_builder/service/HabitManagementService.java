package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.dto.HabitDto;
import io.github.ciamcioo.habit_builder.entity.Habit;
import io.github.ciamcioo.habit_builder.exceptions.HabitAlreadyExistsException;
import io.github.ciamcioo.habit_builder.exceptions.HabitNotFoundException;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HabitManagementService implements HabitService{
    private static final Logger log = LoggerFactory.getLogger(HabitManagementService.class);

    private static final String HABIT_NOT_FOUND = "Habit with name = %s not found";
    private static final String HABIT_ALREADY_EXIST = "Habit with name = %s already exists in database";

    private final HabitRepository habitRepository;

    public HabitManagementService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Override
    public List<HabitDto> getAllHabits() {
        List<HabitDto> habits = new ArrayList<>();

        habitRepository.findAll()
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
                                             () -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND, name))
                                     );

        return convertHabitToHabitDto(habit);
    }

    @Override
    public String addHabit(HabitDto habit) {
        if (habitRepository.existsByName(habit.name())) {
            throw new HabitAlreadyExistsException(String.format(HABIT_ALREADY_EXIST, habit.name()));
        }

        Habit record = new Habit(habit.name(),
                                habit.frequency(),
                                habit.startDate(),
                                habit.endDate(),
                                habit.reminder()
        );

        log.info(record.toString());
        habitRepository.saveAndFlush(record);

        return habit.name();
    }

    @Override
    public List<String> addHabits(HabitDto... habitDtos) {
        List<Habit> habitsToSave = Arrays.stream(habitDtos)
                                            .filter(habitDto ->
                                                !habitRepository.existsByName(habitDto.name())
                                            )
                                            .map(this::convertHabitDtoToHabit)
                                            .toList();

        log.info(habitsToSave.toString());
        habitRepository.saveAllAndFlush(habitsToSave);

        return habitsToSave.stream().map(Habit::getName).toList();
    }

    @Override
    public HabitDto updateHabit(String habitName, HabitDto updatedHabit) {
        Optional<Habit> recordToUpdate = habitRepository.findHabitByName(habitName);
        if (recordToUpdate.isEmpty()) {
            throw new HabitNotFoundException(String.format(HABIT_NOT_FOUND, habitName));
        }

        Optional<Habit> recordWithUpdateName = habitRepository.findHabitByName(updatedHabit.name());
        if (recordWithUpdateName.isPresent()) {
            throw new HabitAlreadyExistsException(String.format(HABIT_ALREADY_EXIST, updatedHabit.name()));
        }

        Habit record = recordToUpdate.get();
        record.setName(updatedHabit.name());
        record.setFrequency(updatedHabit.frequency());
        record.setStartDate(updatedHabit.startDate());
        record.setEndDate(updatedHabit.endDate());
        record.setReminder(updatedHabit.reminder());

        log.info(record.toString());

        habitRepository.saveAndFlush(record);

        return updatedHabit;
    }

    private HabitDto convertHabitToHabitDto(Habit habit) {
        return new HabitDto(habit.getName(),
                            habit.getFrequency(),
                            habit.getStartDate(),
                            habit.getEndDate(),
                            habit.getReminder()
        );
    }

    private Habit convertHabitDtoToHabit(HabitDto habitDto) {
        return new Habit(habitDto.name(),
                        habitDto.frequency(),
                        habitDto.startDate(),
                        habitDto.endDate(),
                        habitDto.reminder()
        );
    }




}
