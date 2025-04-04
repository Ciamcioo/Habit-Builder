package io.github.ciamcioo.habit_builder.service.mapper;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import io.github.ciamcioo.habit_builder.util.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    UserBuilder userBuilder = UserBuilder.getInstance();

    @Test
    @DisplayName("If argument is null the return value should be null for toDto() method")
    void toDtoShouldReturnNull() {
        assertNull(userMapper.toDTO(null));
    }

    @Test
    @DisplayName("If argument is null the return value should be null for toEntity() method")
    void toEntityShouldReturnNull() {
        assertNull(userMapper.toEntity(null));
    }

    @Test
    @DisplayName("Result habitDTO object should have the same values of fields as argument habit object for toDto() method")
    void toDtoShouldReturnHabitDTOWithTheSameValuesOfFieldsAsArgumentHabit() {
        User user = userBuilder.withTestValues().buildUser();

        UserDTO resultUserDTO = userMapper.toDTO(user);

        assertAll(
                () -> assertEquals(user.getUsername(),  resultUserDTO.username()),
                () -> assertEquals(user.getEmail(),     resultUserDTO.email()),
                () -> assertEquals(user.getFirstName(), resultUserDTO.firstName()),
                () -> assertEquals(user.getLastName(),  resultUserDTO.lastName()),
                () -> assertEquals(user.getAge(),       resultUserDTO.age())
        );
    }

    @Test
    @DisplayName("Result Habit object should have the same value of fields as argument habitDTO object after toEntity() method")
    void toEntityShouldReturnHabitObjectWithTheSameValuesOfFieldsAsArgumentHabitDTO() {
        UserDTO userDTO = userBuilder.withTestValues().buildUserDTO();

        User user = userMapper.toEntity(userDTO);

        assertAll(
                () -> assertNull(user.getId()),
                () -> assertNull(user.getUserHabits()),
                () -> assertEquals(userDTO.username(),  user.getUsername()),
                () -> assertEquals(userDTO.email(),     user.getEmail()),
                () -> assertEquals(userDTO.firstName(), user.getFirstName()),
                () -> assertEquals(userDTO.lastName(),  user.getLastName()),
                () -> assertEquals(userDTO.age(),       user.getAge())
        );
    }

}
