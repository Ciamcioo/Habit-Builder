package io.github.ciamcioo.habit_builder.controller;

import io.github.ciamcioo.habit_builder.dto.HabitDto;
import io.github.ciamcioo.habit_builder.entity.Habit;
import io.github.ciamcioo.habit_builder.service.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class HabitController {
    private HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/getAllHabits")
    public ResponseEntity<List<HabitDto>> getAllHabits() {
        return new ResponseEntity<>(
               habitService.getAllHabits(),
               HttpStatus.OK
        );
    }

    @GetMapping("/getHabit")
    public ResponseEntity<HabitDto> getHabitByName(@RequestParam String name) {
        return new ResponseEntity<>(
                habitService.getHabitByName(name),
                HttpStatus.OK);
    }

    @PostMapping("/add/habit")
    public ResponseEntity<String> addHabit(@RequestBody HabitDto habit) {
        return new ResponseEntity<>(
                habitService.addHabit(habit),
                HttpStatus.OK
        );
    }

    @PostMapping("/add/habits")
    public ResponseEntity<List<String>> addHabits(@RequestBody HabitDto... habits) {
        return new ResponseEntity<>(
                habitService.addHabits(habits),
                HttpStatus.OK
        );
    }

    @PutMapping("/update/habit")
    public ResponseEntity<HabitDto> updateHabit(@RequestParam String habitName, @RequestBody HabitDto updatedHabit) {
        return new ResponseEntity<>(
                habitService.updateHabit(habitName, updatedHabit),
                HttpStatus.OK
        );
    }

}
