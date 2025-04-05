package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;
import io.github.ciamcioo.habit_builder.repository.UserRepository;
import io.github.ciamcioo.habit_builder.exception.UserAlreadyExistsException;
import io.github.ciamcioo.habit_builder.exception.UserNotFoundException;
import io.github.ciamcioo.habit_builder.service.mapper.UserMapper;
import io.github.ciamcioo.habit_builder.util.UserBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    public static final String TEST_EMAIL_ADDRESS = "test@gmail.com";
    public static final String TEST_USERNAME      = "FooBarUsername";

    // TESTED SERVICE
    private static UserService    userService;

    // MOCKED SERVICE
    private static UserMapper      userMapper;
    private static UserRepository  userRepository;
    private static HabitRepository habitRepository;

    // HELPER OBJECT
    private static UserBuilder userBuilder = UserBuilder.getInstance();
    private static User        user;
    private static UserDTO     userDTO;


   @BeforeEach
   void beforeEachSetUp() {
       userMapper = mock(UserMapper.class);
       userRepository = mock(UserRepository.class);
       habitRepository = mock(HabitRepository.class);
       userService = new UserManagementService(userRepository, userMapper);

       userBuilder = userBuilder.withTestValues();
       user = userBuilder.withEmail(TEST_EMAIL_ADDRESS).buildUser();
       userDTO = userBuilder.buildUserDTO();
   }


   // GET ALL USERS TEST


   @Test
   @DisplayName("Method getAllUsers() should not return null")
   void getAllUsersReturnNotNullValue() {
       assertNotNull(userService.getAllUsers());
   }

   @Test
   @DisplayName("Method getAllUsers() should return empty List object")
   void getAllUsersReturnEmptyList() {
       when(userRepository.findAll()).thenReturn(List.of());

       assertTrue(userService.getAllUsers().isEmpty());

       verify(userRepository).findAll();
   }

   @Test
   @DisplayName("Method getAllUsers() should return List containing UserDTO object")
   void getAllUsersReturnUserDTOList() {
       List<User> users = List.of(
          userBuilder.buildUser(),
          userBuilder.buildUser()
       );

       when(userRepository.findAll()).thenReturn(users);

       assertFalse(userService.getAllUsers().isEmpty());

       verify(userRepository).findAll();
   }

   // GET SINGLE USER TEST

   @Test
   @DisplayName("Method getUser() cannot return null")
   void getUserCanNotReturnNull() {
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(user));
       when(userMapper.toDTO(user)).thenReturn(userDTO);

       assertNotNull(userService.getUser(TEST_EMAIL_ADDRESS));
   }

   @Test
   @DisplayName("Method getUser() should return user with matching email address to argument email address")
   void returnUserEmailMustMatchGetEmailInArgument() {
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(user));
       when(userMapper.toDTO(user)).thenReturn(userDTO);

       UserDTO resultUser = userService.getUser(TEST_EMAIL_ADDRESS);

       assertEquals(TEST_EMAIL_ADDRESS, resultUser.email());

       verify(userRepository).findUserByEmail(TEST_EMAIL_ADDRESS);
   }

   @Test
   @DisplayName("Method getUser() should return UserDTO matching to test UserDTO")
   void returnUserObjectMustMuchTheTestUserDTOObject() {
       when(userRepository.findUserByEmail(userDTO.email())).thenReturn(Optional.of(user));
       when(userMapper.toDTO(user)).thenReturn(userDTO);

       assertEquals(userDTO, userService.getUser(userDTO.email()));

       verify(userRepository).findUserByEmail(user.getEmail());
   }

   @Test
   @DisplayName("Method getUser() should throw UserNotFoundException if user with provided email do not exists")
   void getUserMethodShouldThrowUserNotFoundException() {
       String testEmail = "not_existing_user_email@gmail.com";

       when(userRepository.findUserByEmail(testEmail)).thenReturn(Optional.empty());

       assertThrows(UserNotFoundException.class, () -> userService.getUser(testEmail));

       verify(userRepository).findUserByEmail(testEmail);
   }

   // ADD SINGLE USER TEST

   @Test
   @DisplayName("Method addUser() should not return the null value")
   void addUserShouldNotReturnNull() {
       when(userMapper.toEntity(userDTO)).thenReturn(user);

       assertNotNull(userService.addUser(userDTO));
   }

   @Test
   @DisplayName("Method addUser() should return the name of created user")
   void addUserShouldReturnTheGetUsernameOfCreatedUser() {
       when(userMapper.toEntity(userDTO)).thenReturn(user);

       assertEquals(userDTO.username(), userService.addUser(userDTO));

       verify(userRepository).saveAndFlush(user);
   }

   @Test
   @DisplayName("Method addUser() should throw user already exists exception if user with specified email already exists.")
   void addUserShouldThrowUserAlreadyExistsException() {
       when(userRepository.findUserByEmail(userDTO.email())).thenReturn(Optional.of(user));

       assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(userDTO));

       verify(userRepository, never()).saveAndFlush(user);
   }

   // ADD MULTIPLE USERS AT ONCE TEST

   @Test
   @DisplayName("Method addUsers() cannot return null value")
   void addUsersShouldNotReturnNullValue() {
       assertNotNull(userService.addUsers(List.of()));
   }

   @Test
   @DisplayName("Method addUsers() should return List of the same size as the argument List")
   void addUsersShouldReturnTheSameListSizeAsArgumentList() {
       List<UserDTO> userDTOs = List.of(
               userBuilder.withTestValues()
                          .buildUserDTO(),
               userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                          .withUsername(TEST_USERNAME)
                          .buildUserDTO()
       );

       List<User> users = List.of(
               userBuilder.withTestValues()
                          .buildUser(),
               userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                          .withUsername(TEST_USERNAME)
                          .buildUser()
       );

       when(userMapper.toEntity(userDTOs.get(0))).thenReturn(users.get(0));
       when(userMapper.toEntity(userDTOs.get(1))).thenReturn(users.get(1));

       assertEquals(userDTOs.size(), userService.addUsers(userDTOs).size());

       verify(userRepository).saveAllAndFlush(anyList());
   }

   @Test
   @DisplayName("Method addUsers() should return String List with usernames of added user")
   void addUsersShouldReturnGetUsernameList() {
       List<UserDTO> userDTOs = List.of(
               userBuilder.withTestValues()
                          .buildUserDTO(),
               userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                          .withUsername(TEST_USERNAME)
                          .buildUserDTO()
       );

       List<User> users = List.of(
               userBuilder.withTestValues()
                          .buildUser(),
               userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                          .withUsername(TEST_USERNAME)
                          .buildUser()
       );

       when(userMapper.toEntity(userDTOs.get(0))).thenReturn(users.get(0));
       when(userMapper.toEntity(userDTOs.get(1))).thenReturn(users.get(1));

       List<String> usernames = userService.addUsers(userDTOs);

       for (UserDTO userDTO : userDTOs) {
           assertTrue(usernames.contains(userDTO.username()));
       }

       verify(userRepository).saveAllAndFlush(anyList());
   }

   @Test
   @DisplayName("Method addUsers() should add only unique users from argument List")
   void addUsersShouldOnlyAddUniqueUsersToDatabase() {
        int expectedSize = 1;
        List<UserDTO> notUniqueUserDTOs = List.of(
              userBuilder.withEmail(TEST_EMAIL_ADDRESS).buildUserDTO(),
              userBuilder.withEmail(TEST_EMAIL_ADDRESS).buildUserDTO()
        );

        User user = userBuilder.buildUser();

        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(habitRepository.findHabitByName(TEST_EMAIL_ADDRESS)).thenReturn(Optional.empty());

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        int resultSize = userService.addUsers(notUniqueUserDTOs).size();

        // Reverse order of assert and verify is needed for proper call of capture()
        verify(userRepository).saveAllAndFlush(captor.capture());

        assertEquals(expectedSize, resultSize);
        assertEquals(expectedSize, captor.getValue().size());
   }

   // UPDATE USER TESTS

   @Test
   @DisplayName("Method updateUser() should not return null value")
   void updateUserShouldNotReturnNullValue() {
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(user));
       when(userMapper.toDTO(user)).thenReturn(userDTO);

       assertNotNull(userService.updateUser(TEST_EMAIL_ADDRESS, userBuilder.buildUserDTO()));
   }

   @Test
   @DisplayName("Method updateUser() should return UserDTO that matches updated object")
   void updateUserShouldReturnUserDTOObjectWhichWillMuchUpdateObject() {
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(user));
       when(userMapper.toDTO(user)).thenReturn(userDTO);

       assertEquals(userDTO, userService.updateUser(TEST_EMAIL_ADDRESS, userDTO));
   }

   @Test
   @DisplayName("Method updateUser() should throw new UserAlreadyExistsException if user tries to updated email to already existing one")
   void updateUserShouldThrowUserAlreadyExistsExceptionIfGetEmailOfUpdatedUserIsAlreadyInDatabase() {
       UserDTO userToUpdate = userDTO;
       String tmpEmailAddress = "otherTestEmail@gmail.com";

       when(userRepository.findUserByEmail(tmpEmailAddress)).thenReturn(Optional.of(new User()));
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(new User()));

       assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(tmpEmailAddress, userToUpdate));
   }

   @Test
   @DisplayName("Method updateUser() throw new UserNotFoundException if User with specific email address doesn't exist")
   void updateUserShouldThrowUserNotFoundException() {
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.empty());

       assertThrows(UserNotFoundException.class, () -> userService.updateUser(TEST_EMAIL_ADDRESS, userDTO));

       verify(userRepository, never()).saveAndFlush(any(User.class));
   }

   // DELETE USER TEST

   @Test
   @DisplayName("Method deleteUser() should throw UserNotFoundException if there is no user for provided email.")
   void deleteUserShouldThrowUserNotFoundException() {
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.empty());

       assertThrows(UserNotFoundException.class, () -> userService.deleteUser(TEST_EMAIL_ADDRESS));

       verify(userRepository, never()).delete(any(User.class));
       verify(userRepository, never()).flush();
   }

   @Test
   @DisplayName("Method deleteUser() should call delete method and flush method on the userRepository interface")
   void deleterUserShouldCallUserRepositoryDeleteAndFlushMethod() {
       when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(user));

       userService.deleteUser(TEST_EMAIL_ADDRESS);

       verify(userRepository).delete(user);
       verify(userRepository).flush();
   }
}
