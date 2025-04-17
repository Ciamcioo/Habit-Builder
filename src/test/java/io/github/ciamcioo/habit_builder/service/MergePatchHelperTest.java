package io.github.ciamcioo.habit_builder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ciamcioo.habit_builder.exception.ProcessingException;
import io.github.ciamcioo.habit_builder.model.entity.User;
import io.github.ciamcioo.habit_builder.util.UserBuilder;

import jakarta.json.Json;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonObject;

import jakarta.json.JsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MergePatchHelperTest {

    // TESTED OBJECT
    MergePatchHelper mergePatchHelper;

    ObjectMapper objectMapper;

    // HELPER OBJECTS
    User user;
    String userJson = "{\"id\":\"0ecfcfad-c6ec-4530-b2e4-14dd4b4586f3\",\"email\":\"fooBar@gmail.com\",\"username\":\"FooBar\",\"firstName\":\"Foo\",\"lastName\":\"Bar\",\"age\":21}";

    UserBuilder userBuilder = UserBuilder.getInstance();

   @BeforeEach
    void setup() throws JsonProcessingException {
        objectMapper = mock(ObjectMapper.class);
        mergePatchHelper = new MergePatchHelper(objectMapper);

        userBuilder = userBuilder.withTestValues();
        user = userBuilder.withId(UUID.fromString("0ecfcfad-c6ec-4530-b2e4-14dd4b4586f3"))
                          .buildUser();

       when(objectMapper.writeValueAsString(user)).thenReturn(userJson);
    }


    @Test
    @DisplayName("Null value should not be returned by the mergePatch method")
    void mergePatchReturnNotNullValue() throws JsonProcessingException {
        User updatedUser = userBuilder.withAge(18)
                                      .buildUser();
        String updateUserJson = "{\"id\":\"0ecfcfad-c6ec-4530-b2e4-14dd4b4586f3\",\"email\":\"fooBar@gmail.com\",\"username\":\"FooBar\",\"firstName\":\"Foo\",\"lastName\":\"Bar\",\"age\":18}";
        JsonObject agePatch = Json.createObjectBuilder()
                                   .add("age", 18)
                                   .build();
        JsonMergePatch mergePatch = Json.createMergePatch(agePatch);

        when(objectMapper.writeValueAsString(any(JsonValue.class))).thenReturn(updateUserJson);
        when(objectMapper.readValue(updateUserJson, User.class)).thenReturn(updatedUser);

        assertNotNull(mergePatchHelper.mergePatch(mergePatch, user, User.class));
    }

    @Test
    @DisplayName("If exception occurs during patch update IllegalArgumentException should be thrown")
    void mergePatchShouldThrowIllegalArgumentException() throws JsonProcessingException {
        User testUser = userBuilder.buildUser();
        String updateUserJson = "{\"id\":\"0ecfcfad-c6ec-4530-b2e4-14dd4b4586f3\",\"email\":\"fooBar@gmail.com\",\"username\":\"FooBar\",\"firstName\":\"Foo\",\"lastName\":\"Bar\",\"age\":18}";
        JsonObject agePatch = Json.createObjectBuilder()
                                  .add("age", 18)
                                  .build();
        JsonMergePatch mergePatch = Json.createMergePatch(agePatch);

        when(objectMapper.writeValueAsString(any(JsonValue.class))).thenReturn(updateUserJson);
        when(objectMapper.readValue(updateUserJson, User.class)).thenThrow(JsonProcessingException.class);

        assertThrows(IllegalArgumentException.class,  () -> mergePatchHelper.mergePatch(mergePatch, testUser, User.class) );
    }


    @Test
    @DisplayName("If entity argument is null, validateEntity method should throw IllegalArgumentException with message")
    void entityArgumentSetToNullThrowIllegalArgumentException() throws NoSuchMethodException {
       Method validateUpdateEntity = MergePatchHelper.class.getDeclaredMethod("validateUpdatedEntity", Object.class, Class.class);
       validateUpdateEntity.setAccessible(true);

       Exception resultException = assertThrows(InvocationTargetException.class, () -> validateUpdateEntity.invoke(mergePatchHelper, null, Object.class));
       assertInstanceOf(IllegalArgumentException.class, resultException.getCause());
       assertNotNull(resultException.getCause().getMessage());
    }

    @Test
    @DisplayName("If entityClass argument is null, validateEntity method should throw IllegalArgumentException with message")
    void entityClassArgumentSetToNullThrowIllegalArgumentException() throws NoSuchMethodException {
        Method validateUpdateEntity = MergePatchHelper.class.getDeclaredMethod("validateUpdatedEntity", Object.class, Class.class);
        validateUpdateEntity.setAccessible(true);

        Exception resultException = assertThrows(InvocationTargetException.class, () -> validateUpdateEntity.invoke(mergePatchHelper, new Object(), null));
        assertInstanceOf(IllegalArgumentException.class, resultException.getCause());
        assertNotNull(resultException.getCause().getMessage());
    }

    @Test
    @DisplayName("If entity and entityClass argument represent different classes, validateEntity method should throw IllegalArgumentException with a message")
    void entityAndEntityClassArgumentHaveDifferentClassesThrowIllegalArgumentException() throws NoSuchMethodException {
        Method validateUpdateEntity = MergePatchHelper.class.getDeclaredMethod("validateUpdatedEntity", Object.class, Class.class);
        validateUpdateEntity.setAccessible(true);

        Exception resultException = assertThrows(InvocationTargetException.class, () -> validateUpdateEntity.invoke(mergePatchHelper, new Object(), User.class));
        assertInstanceOf(IllegalArgumentException.class, resultException.getCause());
        assertNotNull(resultException.getCause().getMessage());
    }

    @Test
    @DisplayName("If method convertTargetToString finishes without exception it should return not empty String value")
    void convertObjectToStringShouldReturnString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, JsonProcessingException {
        Method convertTargetToString = MergePatchHelper.class.getDeclaredMethod("convertTargetToString", Object.class);
        convertTargetToString.setAccessible(true);

        assertEquals(userJson, (String) convertTargetToString.invoke(mergePatchHelper, user));
    }

    @Test
    @DisplayName("If in method convertTargetToString occurs JsonProcessingException(), it should handle it and throw new ProcessingException with a message")
    void convertObjectToStringShouldReturnMatchingStringStructForTarget() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, JsonProcessingException {
        Method convertTargetToString = MergePatchHelper.class.getDeclaredMethod("convertTargetToString", Object.class);
        convertTargetToString.setAccessible(true);

        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Processing failed") {});

        Exception resultException = assertThrows(InvocationTargetException.class, () -> convertTargetToString.invoke(mergePatchHelper, new Object()));
        assertInstanceOf(ProcessingException.class, resultException.getCause());
        assertNotNull(resultException.getCause().getMessage());
    }
}
