package io.github.ciamcioo.habit_builder.util;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserBuildTest {
    // CONSTANT
    public static final String  DEF_USERNAME   = "FooBar";
    public static final String  DEF_FIRST_NAME = "Foo";
    public static final String  DEF_LAST_NAME  = "Bar";
    public static final Integer DEF_AGE        = 21;
    public static final String  DEF_EMAIL      = "fooBar@gmail.com";

    public static final UUID    ID          = UUID.randomUUID();
    public static final String  USERNAME    = "BarFoo";
    public static final String  EMAIL       = "barfoo@gov.com";
    public static final String  FIRST_NAME  = "Bar";
    public static final String  LAST_NAME   = "Foo";
    public static final Integer AGE         = 99;


    // TESTED SERVICE
    private static UserBuilder userBuilder = UserBuilder.getInstance();

    @BeforeEach
    void setup() {
        userBuilder = userBuilder.withTestValues();
    }

    @Test
    @DisplayName("Method buildUser() should return User instance with default values")
    void buildUserShouldReturnUserObjectWitTestValues() {
        User builtUser = userBuilder.buildUser();

        assertAll(
                () -> assertInstanceOf(UUID.class, builtUser.getId()),
                () -> assertEquals(DEF_EMAIL,      builtUser.getEmail()),
                () -> assertEquals(DEF_USERNAME,   builtUser.getUsername()),
                () -> assertEquals(DEF_FIRST_NAME, builtUser.getFirstName()),
                () -> assertEquals(DEF_LAST_NAME,  builtUser.getLastName()),
                () -> assertEquals(DEF_AGE,        builtUser.getAge())
        );
    }

    @Test
    @DisplayName("Method buildUser() should return User instance with fields containing values set before build")
    void buildUserShouldReturnUserObjectWithValuesSetsBeforeBuildMethod() {
        User builtUser = userBuilder.withId(ID)
                                    .withEmail(EMAIL)
                                    .withUsername(USERNAME)
                                    .withFirstName(FIRST_NAME)
                                    .withLastName(LAST_NAME)
                                    .withAge(AGE)
                                    .buildUser();

        assertAll(
                () -> assertEquals(ID,         builtUser.getId()),
                () -> assertEquals(EMAIL,      builtUser.getEmail()),
                () -> assertEquals(USERNAME,   builtUser.getUsername()),
                () -> assertEquals(FIRST_NAME, builtUser.getFirstName()),
                () -> assertEquals(LAST_NAME,  builtUser.getLastName()),
                () -> assertEquals(AGE,        builtUser.getAge())
        );
    }

    @Test
    @DisplayName("Method buildUserDTO  should return UserDTO instance with default values")
    void buildUserDTOWithDefaultValues() {
        UserDTO builtUserDto = userBuilder.buildUserDTO();

        assertAll(
                () -> assertEquals(DEF_USERNAME,   builtUserDto.username()),
                () -> assertEquals(DEF_EMAIL,      builtUserDto.email()),
                () -> assertEquals(DEF_FIRST_NAME, builtUserDto.firstName()),
                () -> assertEquals(DEF_LAST_NAME,  builtUserDto.lastName()),
                () -> assertEquals(DEF_AGE,        builtUserDto.age())
        );
    }

    @Test
    @DisplayName("Method buildUserDTO() should return UserDTO instance with fields containing values set before build")
    void buildUserDtoWithPreviouslySetValues() {
        UserDTO builtUserDTO = userBuilder.withUsername(USERNAME)
                                          .withEmail(EMAIL)
                                          .withFirstName(FIRST_NAME)
                                          .withLastName(LAST_NAME)
                                          .withAge(AGE)
                                          .buildUserDTO();

        assertAll(
                () -> assertEquals(USERNAME,   builtUserDTO.username()),
                () -> assertEquals(EMAIL,      builtUserDTO.email()),
                () -> assertEquals(FIRST_NAME, builtUserDTO.firstName()),
                () -> assertEquals(LAST_NAME,  builtUserDTO.lastName()),
                () -> assertEquals(AGE,        builtUserDTO.age())
        );
    }


}
