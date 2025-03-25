package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import io.github.ciamcioo.habit_builder.repository.UserRepository;
import io.github.ciamcioo.habit_builder.service.exceptions.ConversionException;
import io.github.ciamcioo.habit_builder.util.UserBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class UserManagementServiceTest {
    public static final String CONVERSION_EXCEPTION_FROM_USER_DTO_TO_USER  = "Conversion of UserDTO to User ended up with failure!";
    public static final String CONVERSION_EXCEPTION_FROM_USER_TO_USER_DTO  = "Conversion of User to UserDTO ended up with failure!";

    private static UserManagementService userManagementService;
    private static UserBuilder           userBuilder;

    @BeforeAll
    static void setup() {
        UserRepository userRepository = mock(UserRepository.class);
        userManagementService = new UserManagementService(userRepository);

        userBuilder = UserBuilder.getInstance();
    }

    @BeforeEach
    void setupBeforeTest() {
        userBuilder = userBuilder.withTestValues();
    }

    @Test
    @DisplayName("Method convertUserToUserDTO() should not return null")
    void convertUserToUserDTOShouldNotReturnNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserToUserDTO = userManagementService.getClass().getDeclaredMethod("convertUserToUserDTO", User.class);
        convertUserToUserDTO.setAccessible(true);

        User user = userBuilder.buildUser();
        Object resultObject = convertUserToUserDTO.invoke(userManagementService, user);

        assertNotNull(resultObject);
    }

    @Test
    @DisplayName("Method convertUserToUserDTO should return UserDTO instance which fields match User input object fields")
    void convertUserToUserDTOShouldReturnUseDTOMatchingUserInstance() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method convertUserToUserDTO = userManagementService.getClass().getDeclaredMethod("convertUserToUserDTO", User.class);
        convertUserToUserDTO.setAccessible(true);

        User user = userBuilder.buildUser();
        UserDTO userDTO = (UserDTO) convertUserToUserDTO.invoke(userManagementService, user);

        assertAll(
                () -> assertEquals(user.username(), userDTO.username()),
                () -> assertEquals(user.firstName(), userDTO.firstName()),
                () -> assertEquals(user.lastName(), userDTO.lastName()),
                () -> assertEquals(user.age(), userDTO.age())
        );
    }

    @Test
    @DisplayName("Method convertUserToUserDTO should throw ConversionException if Runtime exception occurs with appropriate exception message")
    void convertUserToUserDTOShouldThrowConversionException() throws NoSuchMethodException {
        Method convertUserToUserDTO = userManagementService.getClass().getDeclaredMethod("convertUserToUserDTO", User.class);
        convertUserToUserDTO.setAccessible(true);

        User notValidUser = null;

        Exception exception = assertThrows(InvocationTargetException.class, () -> convertUserToUserDTO.invoke(userManagementService, notValidUser));
        assertInstanceOf(ConversionException.class, exception.getCause());
        assertEquals(CONVERSION_EXCEPTION_FROM_USER_TO_USER_DTO, exception.getCause().getMessage());

    }

    @Test
    @DisplayName("Method convertUserListToUserDTOList() should not return null value")
    void convertUserListToUserDTOListShouldNotReturnNullValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserListToUserDTOList = UserManagementService.class.getDeclaredMethod("convertUserListToUserDTOList", List.class);
        convertUserListToUserDTOList.setAccessible(true);

        List<User> userList = List.of(
                userBuilder.withUsername("FooBar1").buildUser(),
                userBuilder.withUsername("FooBar2").buildUser()
        );

        Object resultList = convertUserListToUserDTOList.invoke(userManagementService, userList);

        assertNotNull(resultList);
    }

    @Test
    @DisplayName("Method convertUserListToUserDTOList() should return List instance of the same size as input List object")
    void userListAndUserDTOListShouldHaveTheSameSize() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method convertUserListToUserDTOList = UserManagementService.class.getDeclaredMethod("convertUserListToUserDTOList", List.class);
        convertUserListToUserDTOList.setAccessible(true);

        List<User> userList = List.of(
                userBuilder.withUsername("FooBar1").buildUser(),
                userBuilder.withUsername("FooBar2").buildUser()
        );

        Object result = convertUserListToUserDTOList.invoke(userManagementService, userList);
        assertNotNull(result);
        assertInstanceOf(List.class, result);
        List<?> resultList = (List<?>) result;

        assertFalse(resultList.isEmpty());
        assertInstanceOf(UserDTO.class, resultList.getFirst());


        @SuppressWarnings("unchecked")
        List<UserDTO> userDTOs = (List<UserDTO>) resultList;

        assertEquals(userList.size(), userDTOs.size());
    }

    @Test
    @DisplayName("Method convertUserListToUserDTOList() should return UserDTO List with objects matching to objects stored in User List")
    void userDtoListShouldContainsMatchingObjectToObjectsInUserList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserListToUserDTOList = UserManagementService.class.getDeclaredMethod("convertUserListToUserDTOList", List.class);
        convertUserListToUserDTOList.setAccessible(true);

        List<User> userList = List.of(
                userBuilder.withUsername("FooBar1").buildUser(),
                userBuilder.withUsername("FooBar2").buildUser()
        );

        Object result = convertUserListToUserDTOList.invoke(userManagementService, userList);
        assertNotNull(result);
        assertInstanceOf(List.class, result);
        List<?> resultList = (List<?>) result;

        assertFalse(resultList.isEmpty());
        assertInstanceOf(UserDTO.class, resultList.getFirst());


        @SuppressWarnings("unchecked")
        List<UserDTO> userDTOs = (List<UserDTO>) resultList;

        for (int i = 0; i < userDTOs.size(); i++) {
            User user = userList.get(i);
            UserDTO userDTO = userDTOs.get(i);
            assertAll(
                    () -> assertEquals(user.username(), userDTO.username()),
                    () -> assertEquals(user.firstName(), userDTO.firstName()),
                    () -> assertEquals(user.lastName(), userDTO.lastName()),
                    () -> assertEquals(user.age(), userDTO.age())
            );
        }

    }

    @Test
    @DisplayName("Method convertUserListToUserListDTO must return the immutable collection")
    void convertUserListToUserDTOListShouldReturnImmutableCollection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserListToUserDTOList = UserManagementService.class.getDeclaredMethod("convertUserListToUserDTOList", List.class);
        convertUserListToUserDTOList.setAccessible(true);

        List<User> userList = List.of(
                userBuilder.withUsername("FooBar1").buildUser(),
                userBuilder.withUsername("FooBar2").buildUser()
        );

        Object result = convertUserListToUserDTOList.invoke(userManagementService, userList);
        assertNotNull(result);
        assertInstanceOf(List.class, result);
        List<?> resultList = (List<?>) result;

        assertFalse(resultList.isEmpty());
        assertInstanceOf(UserDTO.class, resultList.getFirst());


        @SuppressWarnings("unchecked")
        List<UserDTO> userDTOs = (List<UserDTO>) resultList;

        UserDTO userDTO = userBuilder.withTestValues().buildUserDTO();

        assertThrows(UnsupportedOperationException.class, () -> userDTOs.add(userDTO));
    }

    @Test
    @DisplayName("Method convertUserDTOToUser cannot return null")
    void convertUserDTOToUserCanNotReturnNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method convertUserDTOToUser = UserManagementService.class.getDeclaredMethod("convertUserDTOToUser", UserDTO.class);
        convertUserDTOToUser.setAccessible(true);

        UserDTO userDTO = userBuilder.withTestValues()
                        .buildUserDTO();

        assertNotNull(convertUserDTOToUser.invoke(userManagementService, userDTO));
    }

    @Test
    @DisplayName("Method convertUserDTOToUser must return User object with the same fields values as input UserDTO object")
    void convertUserDTOToUserMustReturnObjectWithSameValuesAsArgument() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserDTOToUser = UserManagementService.class.getDeclaredMethod("convertUserDTOToUser", UserDTO.class);
        convertUserDTOToUser.setAccessible(true);

        UserDTO userDTO = userBuilder.withTestValues()
                                        .buildUserDTO();

        User resultUser = (User) convertUserDTOToUser.invoke(userManagementService, userDTO); assertAll(
                () -> assertNull(resultUser.id()),
                () -> assertEquals(userDTO.email(), resultUser.email()),
                () -> assertEquals(userDTO.username(), resultUser.username()),
                () -> assertEquals(userDTO.firstName(), resultUser.firstName()),
                () -> assertEquals(userDTO.lastName(), resultUser.lastName()),
                () -> assertEquals(userDTO.age(), resultUser.age())
        );
    }

    @Test
    @DisplayName("Method convertUserDTOToUser should throw ConversionException if Runtime Exception occurs with appropriate exception message")
    void convertUserDTOToUserShouldThrowConversionException() throws NoSuchMethodException {
        Method convertUserDTOToUser = UserManagementService.class.getDeclaredMethod("convertUserDTOToUser", UserDTO.class);
        convertUserDTOToUser.setAccessible(true);

        UserDTO userDTO = null;

        Exception exception = assertThrows(InvocationTargetException.class, () -> convertUserDTOToUser.invoke(userManagementService, userDTO));
        assertInstanceOf(ConversionException.class, exception.getCause());
        assertEquals(CONVERSION_EXCEPTION_FROM_USER_DTO_TO_USER, exception.getCause().getMessage());

    }

    @Test
    @DisplayName("Method convertUserDTOListToUserList cannot return null value")
    void convertUserDTOListToUserList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserDTOListToUserList = UserManagementService.class.getDeclaredMethod("convertUserDTOListToUserList", List.class);
        convertUserDTOListToUserList.setAccessible(true);

        assertNotNull(convertUserDTOListToUserList.invoke(userManagementService, List.of()));
    }

    @Test
    @DisplayName("Method convertUserDTOListToUserList should return the list of the same size as argument List")
    void argumentAndConvertedListShouldHaveTheSameSizes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserDTOListToUserList = UserManagementService.class.getDeclaredMethod("convertUserDTOListToUserList", List.class);
        convertUserDTOListToUserList.setAccessible(true);

        List<UserDTO> userDTOList = List.of(
                userBuilder.buildUserDTO(),
                userBuilder.buildUserDTO(),
                userBuilder.buildUserDTO()
        );

        Object resultObject = convertUserDTOListToUserList.invoke(userManagementService, userDTOList);
        assertNotNull(resultObject);
        assertInstanceOf(List.class, resultObject);

        List<?> resultList = (List<?>) resultObject;
        assertFalse(resultList.isEmpty());
        assertInstanceOf(User.class, resultList.getFirst());

        @SuppressWarnings("unchecked")
        List<User> resultUserList = (List<User>) resultList;

        assertEquals(userDTOList.size(), resultUserList.size());
    }

    @Test
    @DisplayName("Method convertUserDTOListToUSerList should return User List whom fields are the same as the UserDTO objects")
    void convertUserDTOToUserListShouldContainUsersWithSameFieldsValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserDTOListToUserList = UserManagementService.class.getDeclaredMethod("convertUserDTOListToUserList", List.class);
        convertUserDTOListToUserList.setAccessible(true);

        List<UserDTO> userDTOList = List.of(
                userBuilder.buildUserDTO(),
                userBuilder.withEmail("example@gmail.com").buildUserDTO(),
                userBuilder.withAge(50).buildUserDTO()
        );

        Object resultObject = convertUserDTOListToUserList.invoke(userManagementService, userDTOList);
        assertNotNull(resultObject);
        assertInstanceOf(List.class, resultObject);

        List<?> resultList = (List<?>) resultObject;
        assertFalse(resultList.isEmpty());
        assertInstanceOf(User.class, resultList.getFirst());

        @SuppressWarnings("unchecked")
        List<User> resultUserList = (List<User>) resultList;

        for (int i = 0; i < resultUserList.size(); i++) {
            User user = resultUserList.get(i);
            UserDTO userDTO = userDTOList.get(i);
            assertAll(
                    () -> assertEquals(userDTO.username(), user.username()),
                    () -> assertEquals(userDTO.firstName(), user.firstName()),
                    () -> assertEquals(userDTO.lastName(), user.lastName()),
                    () -> assertEquals(userDTO.age(), user.age())
            );
        }
    }

    @Test
    @DisplayName("Method convertUserDTOListToUserList() should return immutable collection")
    void convertUserDTOListToUserListShouldReturnImmutableCollection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertUserDTOListToUserList = UserManagementService.class.getDeclaredMethod("convertUserDTOListToUserList", List.class);
        convertUserDTOListToUserList.setAccessible(true);

        List<UserDTO> userDTOList = List.of(
                userBuilder.buildUserDTO(),
                userBuilder.withEmail("example@gmail.com").buildUserDTO(),
                userBuilder.withAge(50).buildUserDTO()
        );

        Object resultObject = convertUserDTOListToUserList.invoke(userManagementService, userDTOList);
        assertNotNull(resultObject);
        assertInstanceOf(List.class, resultObject);

        List<?> resultList = (List<?>) resultObject;
        assertFalse(resultList.isEmpty());
        assertInstanceOf(User.class, resultList.getFirst());

        @SuppressWarnings("unchecked")
        List<User> resultUserList = (List<User>) resultList;

        assertThrows(UnsupportedOperationException.class, () -> resultUserList.add(new User()));
    }

}
