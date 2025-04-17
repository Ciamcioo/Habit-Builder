package io.github.ciamcioo.habit_builder.controller;

import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import io.github.ciamcioo.habit_builder.service.HabitService;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodLogging;
import jakarta.json.JsonMergePatch;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class HabitController {
    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/habits")
    @EnableMethodLogging
    public ResponseEntity<List<HabitDTO>> getAllHabits() {
        return new ResponseEntity<>(
               habitService.getAllHabits(),
               HttpStatus.OK
        );
    }

    @GetMapping("/habit/{name}")
    @EnableMethodLogging
    public ResponseEntity<HabitDTO> getHabitByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(
                habitService.getHabitByName(name),
                HttpStatus.OK);
    }

    @PostMapping("/habit")
    @EnableMethodLogging
    public ResponseEntity<String> addHabit(@RequestBody @Valid HabitDTO habit) {
        return new ResponseEntity<>(
                habitService.addHabit(habit),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/habits")
    @EnableMethodLogging
    public ResponseEntity<List<String>> addHabits(@RequestBody @Valid HabitDTO... habits) {
        return new ResponseEntity<>(
                habitService.addHabits(habits),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/habit/{name}")
    @EnableMethodLogging
    public ResponseEntity<HabitDTO> updateHabit(@PathVariable("name") String habitName, @RequestBody @Valid HabitDTO updatedHabit) {
        return new ResponseEntity<>(
                habitService.updateHabit(habitName, updatedHabit),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/habit/{name}", consumes =  "application/merge-patch+json")
    @EnableMethodLogging
    public ResponseEntity<HabitDTO> partialUpdateHabit(@PathVariable("name") String habitName, @RequestBody JsonMergePatch fieldsToUpdate) {
        return new ResponseEntity<>(
                habitService.partialHabitUpdate(habitName, fieldsToUpdate),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/habit/{name}")
    @EnableMethodLogging
    public ResponseEntity<String> deleteHabit(@PathVariable("name") String name) {
        habitService.deleteHabit(name);
        return new ResponseEntity<>(
            "Habit was deleted",
                HttpStatus.NO_CONTENT
        );
    }



}
