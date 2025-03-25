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
    public static final String  DEF_USERNAME   = "FooBar";
    public static final String  DEF_FIRST_NAME = "Foo";
    public static final String  DEF_LAST_NAME  = "Bar";
    public static final Integer DEF_AGE        = 21;
    public static final String  DEF_EMAIL      = "fooBar@gmail.com";

    public static final String  USERNAME    = "BarFoo";
    public static final String  EMAIL       = "barfoo@gov.com";
    public static final String  FIRST_NAME  = "Bar";
    public static final String  LAST_NAME   = "Foo";
    public static final int     AGE         = 99;


    private static UserBuilder userBuilder = UserBuilder.getInstance();

    @BeforeEach
    void setup() {
        userBuilder = userBuilder.withTestValues();
    }

    @Test
    @DisplayName("Method buildUser() should return User instance with default values")
    void buildUserShouldReturnUserObjectWitTestValues() {
        userBuilder = userBuilder.withTestValues();
        User user = userBuilder.buildUser();

        assertAll(
                () -> assertInstanceOf(UUID.class, user.getId()),
                () -> assertEquals(DEF_EMAIL, user.getEmail()),
                () -> assertEquals(DEF_USERNAME, user.getUsername()),
                () -> assertEquals(DEF_FIRST_NAME, user.getFirstName()),
                () -> assertEquals(DEF_LAST_NAME, user.getLastName()),
                () -> assertEquals(DEF_AGE, user.getAge())
        );
    }

    @Test
    @DisplayName("Method buildUser() should return User instance with fields containing values set before build")
    void buildUserShouldReturnUserObjectWithValuesSetsBeforeBuildMethod() {
        UUID randomUUID = UUID.randomUUID();
        userBuilder = userBuilder.withId(randomUUID)
                .withEmail(EMAIL)
                .withUsername(USERNAME)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withAge(AGE);

        User user = userBuilder.buildUser();

        assertAll(
                () -> assertEquals(randomUUID, user.getId()),
                () -> assertEquals(EMAIL, user.getEmail()),
                () -> assertEquals(USERNAME, user.getUsername()),
                () -> assertEquals(FIRST_NAME, user.getFirstName()),
                () -> assertEquals(LAST_NAME, user.getLastName()),
                () -> assertEquals(AGE, user.getAge())
        );
    }

    @Test
    @DisplayName("Method buildUserDTO  should return UserDTO instance with default values")
    void buildUserDTOWithDefaultValues() {
        UserDTO userDTO = userBuilder.buildUserDTO();

        assertAll(
                () -> assertEquals(DEF_USERNAME, userDTO.username()),
                () -> assertEquals(DEF_EMAIL, userDTO.email()),
                () -> assertEquals(DEF_FIRST_NAME, userDTO.firstName()),
                () -> assertEquals(DEF_LAST_NAME, userDTO.lastName()),
                () -> assertEquals(DEF_AGE, userDTO.age())
        );
    }

    @Test
    @DisplayName("Method buildUserDTO() should return UserDTO instance with fields containing values set before build")
    void buildUserDtoWithPreviouslySetValues() {
        userBuilder = userBuilder.withUsername(USERNAME)
                .withEmail(EMAIL)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withAge(AGE);

        UserDTO userDTO = userBuilder.buildUserDTO();

        assertAll(
                () -> assertEquals(USERNAME, userDTO.username()),
                () -> assertEquals(EMAIL, userDTO.email()),
                () -> assertEquals(FIRST_NAME, userDTO.firstName()),
                () -> assertEquals(LAST_NAME, userDTO.lastName()),
                () -> assertEquals(AGE, userDTO.age())
        );
    }


}
