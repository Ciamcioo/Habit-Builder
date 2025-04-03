package io.github.ciamcioo.habit_builder.controller;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.service.UserService;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodLogging;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    @EnableMethodLogging
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(
                userService.getAllUsers(),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/{email}")
    @EnableMethodLogging
    public ResponseEntity<UserDTO> getUser(@PathVariable("email") @Valid String  email) {
        return new ResponseEntity<>(
                userService.getUser(email),
                HttpStatus.OK
        );

    }

    @PostMapping("/user")
    @EnableMethodLogging
    public ResponseEntity<String> addUser(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(
                userService.addUser(userDTO),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/users")
    @EnableMethodLogging
    public ResponseEntity<List<String>> addUsers(@RequestBody @Valid UserDTO... userDTOs) {
        return new ResponseEntity<>(
                userService.addUsers(Arrays.asList(userDTOs)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/user/{email}")
    @EnableMethodLogging
    public ResponseEntity<UserDTO> updateUser(@PathVariable("email") String email,@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(
                userService.updateUser(email, userDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/user/{email}")
    @EnableMethodLogging
    public ResponseEntity<Object> deleteUser(@PathVariable("email") String email) {
        userService.deleteUser(email);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }



}
