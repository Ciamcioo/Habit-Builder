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

    private static Method convertUserToUserDTO;
    private static Method convertUserDTOToUser;
    private static Method convertUserListToUserDTOList;
    private static Method convertUserDTOListToUserList;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        UserRepository userRepository = mock(UserRepository.class);
        userManagementService = new UserManagementService(userRepository);

        userBuilder = UserBuilder.getInstance();

        convertUserToUserDTO = userManagementService.getClass().getDeclaredMethod("convertUserToUserDTO", User.class);
        convertUserToUserDTO.setAccessible(true);

        convertUserDTOToUser = UserManagementService.class.getDeclaredMethod("convertUserDTOToUser", UserDTO.class);
        convertUserDTOToUser.setAccessible(true);

        convertUserListToUserDTOList = UserManagementService.class.getDeclaredMethod("convertUserListToUserDTOList", List.class);
        convertUserListToUserDTOList.setAccessible(true);

        convertUserDTOListToUserList = UserManagementService.class.getDeclaredMethod("convertUserDTOListToUserList", List.class);
        convertUserDTOListToUserList.setAccessible(true);
    }

    @BeforeEach
    void setupBeforeTest() {
        userBuilder = userBuilder.withTestValues();
    }

    @Test
    @DisplayName("Method convertUserToUserDTO() should not return null")
    void convertUserToUserDTOShouldNotReturnNull() throws InvocationTargetException, IllegalAccessException {
        User user = userBuilder.buildUser();
        Object resultObject = convertUserToUserDTO.invoke(userManagementService, user);

        assertNotNull(resultObject);
    }

    @Test
    @DisplayName("Method convertUserToUserDTO should return UserDTO instance which fields match User input object fields")
    void convertUserToUserDTOShouldReturnUseDTOMatchingUserInstance() throws InvocationTargetException, IllegalAccessException {
        User user = userBuilder.buildUser();
        UserDTO userDTO = (UserDTO) convertUserToUserDTO.invoke(userManagementService, user);

        assertAll(
                () -> assertEquals(user.getUsername(), userDTO.username()),
                () -> assertEquals(user.getFirstName(), userDTO.firstName()),
                () -> assertEquals(user.getLastName(), userDTO.lastName()),
                () -> assertEquals(user.getAge(), userDTO.age())
        );
    }

    @Test
    @DisplayName("Method convertUserToUserDTO should throw ConversionException if Runtime exception occurs with appropriate exception message")
    void convertUserToUserDTOShouldThrowConversionException() {
        User notValidUser = null;

        Exception exception = assertThrows(InvocationTargetException.class, () -> convertUserToUserDTO.invoke(userManagementService, notValidUser));
        assertInstanceOf(ConversionException.class, exception.getCause());
        assertEquals(CONVERSION_EXCEPTION_FROM_USER_TO_USER_DTO, exception.getCause().getMessage());

    }

    @Test
    @DisplayName("Method convertUserListToUserDTOList() should not return null value")
    void convertUserListToUserDTOListShouldNotReturnNullValue() throws InvocationTargetException, IllegalAccessException {
        List<User> userList = List.of(
                userBuilder.withUsername("FooBar1").buildUser(),
                userBuilder.withUsername("FooBar2").buildUser()
        );

        Object resultList = convertUserListToUserDTOList.invoke(userManagementService, userList);

        assertNotNull(resultList);
    }

    @Test
    @DisplayName("Method convertUserListToUserDTOList() should return List instance of the same size as input List object")
    void userListAndUserDTOListShouldHaveTheSameSize() throws InvocationTargetException, IllegalAccessException {
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
    void userDtoListShouldContainsMatchingObjectToObjectsInUserList() throws InvocationTargetException, IllegalAccessException {
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
                    () -> assertEquals(user.getUsername(), userDTO.username()),
                    () -> assertEquals(user.getFirstName(), userDTO.firstName()),
                    () -> assertEquals(user.getLastName(), userDTO.lastName()),
                    () -> assertEquals(user.getAge(), userDTO.age())
            );
        }

    }

    @Test
    @DisplayName("Method convertUserListToUserListDTO must return the immutable collection")
    void convertUserListToUserDTOListShouldReturnImmutableCollection() throws InvocationTargetException, IllegalAccessException {
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
    void convertUserDTOToUserCanNotReturnNull() throws InvocationTargetException, IllegalAccessException {
        UserDTO userDTO = userBuilder.withTestValues()
                        .buildUserDTO();

        assertNotNull(convertUserDTOToUser.invoke(userManagementService, userDTO));
    }

    @Test
    @DisplayName("Method convertUserDTOToUser must return User object with the same fields values as input UserDTO object")
    void convertUserDTOToUserMustReturnObjectWithSameValuesAsArgument() throws InvocationTargetException, IllegalAccessException {
        UserDTO userDTO = userBuilder.withTestValues()
                                        .buildUserDTO();

        User resultUser = (User) convertUserDTOToUser.invoke(userManagementService, userDTO); assertAll(
                () -> assertNull(resultUser.getId()),
                () -> assertEquals(userDTO.email(), resultUser.getEmail()),
                () -> assertEquals(userDTO.username(), resultUser.getUsername()),
                () -> assertEquals(userDTO.firstName(), resultUser.getFirstName()),
                () -> assertEquals(userDTO.lastName(), resultUser.getLastName()),
                () -> assertEquals(userDTO.age(), resultUser.getAge())
        );
    }

    @Test
    @DisplayName("Method convertUserDTOToUser should throw ConversionException if Runtime Exception occurs with appropriate exception message")
    void convertUserDTOToUserShouldThrowConversionException() {
        UserDTO userDTO = null;

        Exception exception = assertThrows(InvocationTargetException.class, () -> convertUserDTOToUser.invoke(userManagementService, userDTO));
        assertInstanceOf(ConversionException.class, exception.getCause());
        assertEquals(CONVERSION_EXCEPTION_FROM_USER_DTO_TO_USER, exception.getCause().getMessage());

    }

    @Test
    @DisplayName("Method convertUserDTOListToUserList cannot return null value")
    void convertUserDTOListToUserList() throws InvocationTargetException, IllegalAccessException {
        assertNotNull(convertUserDTOListToUserList.invoke(userManagementService, List.of()));
    }

    @Test
    @DisplayName("Method convertUserDTOListToUserList should return the list of the same size as argument List")
    void argumentAndConvertedListShouldHaveTheSameSizes() throws InvocationTargetException, IllegalAccessException {
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
    void convertUserDTOToUserListShouldContainUsersWithSameFieldsValues() throws InvocationTargetException, IllegalAccessException {
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
                    () -> assertEquals(userDTO.username(), user.getUsername()),
                    () -> assertEquals(userDTO.firstName(), user.getFirstName()),
                    () -> assertEquals(userDTO.lastName(), user.getLastName()),
                    () -> assertEquals(userDTO.age(), user.getAge())
            );
        }
    }

    @Test
    @DisplayName("Method convertUserDTOListToUserList() should return immutable collection")
    void convertUserDTOListToUserListShouldReturnImmutableCollection() throws InvocationTargetException, IllegalAccessException {
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
