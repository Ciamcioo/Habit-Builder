package io.github.ciamcioo.habit_builder.controller;

import io.github.ciamcioo.habit_builder.dto.HabitDto;
import io.github.ciamcioo.habit_builder.entity.Habit;
import io.github.ciamcioo.habit_builder.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping("/habits")
    public ResponseEntity<List<HabitDto>> getAllHabits() {
        return new ResponseEntity<>(
               habitService.getAllHabits(),
               HttpStatus.OK
        );
    }

    @GetMapping("/habit/{name}")
    public ResponseEntity<HabitDto> getHabitByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(
                habitService.getHabitByName(name),
                HttpStatus.OK);
    }

    @PostMapping("/habit")
    public ResponseEntity<String> addHabit(@RequestBody @Valid HabitDto habit) {
        return new ResponseEntity<>(
                habitService.addHabit(habit),
                HttpStatus.OK
        );
    }

    @PostMapping("/habits")
    public ResponseEntity<List<String>> addHabits(@RequestBody @Valid HabitDto... habits) {
        return new ResponseEntity<>(
                habitService.addHabits(habits),
                HttpStatus.OK
        );
    }

    @PutMapping("/habit/{name}")
    public ResponseEntity<HabitDto> updateHabit(@PathVariable("name") String habitName, @RequestBody @Valid HabitDto updatedHabit) {
        return new ResponseEntity<>(
                habitService.updateHabit(habitName, updatedHabit),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/habit/{name}")
    public ResponseEntity<String> deleteHabit(@PathVariable("name") String name) {
        habitService.deleteHabit(name);
        return new ResponseEntity<>(
            "Habit was deleted",
                HttpStatus.ACCEPTED
        );
    }

}
