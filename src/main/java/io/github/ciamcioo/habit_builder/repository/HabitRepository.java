package io.github.ciamcioo.habit_builder.repository;

import io.github.ciamcioo.habit_builder.model.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Integer> {

    Optional<Habit> findHabitByName(String name);

    Boolean existsByName(String name);

}
