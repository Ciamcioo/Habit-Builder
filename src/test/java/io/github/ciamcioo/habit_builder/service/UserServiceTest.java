package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import io.github.ciamcioo.habit_builder.repository.HabitRepository;
import io.github.ciamcioo.habit_builder.repository.UserRepository;
import io.github.ciamcioo.habit_builder.service.exceptions.UserAlreadyExistsException;
import io.github.ciamcioo.habit_builder.service.exceptions.UserNotFoundException;
import io.github.ciamcioo.habit_builder.util.UserBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
   public static final String TEST_EMAIL_ADDRESS = "test@gmail.com";
   public static final String TEST_USERNAME      = "FooBarUsername";

   private static UserService    userService;
   private static UserRepository userRepository;
   private static UserBuilder    userBuilder = UserBuilder.getInstance();

   private HabitRepository habitRepository;

   @BeforeEach
   void beforeEachSetUp() {
      userRepository = mock(UserRepository.class);
      userService = new UserManagementService(userRepository);
      userBuilder = userBuilder.withTestValues();

      habitRepository = mock(HabitRepository.class);
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
   }

   // GET SINGLE USER TEST

   @Test
   @DisplayName("Method getUser() cannot return null")
   void getUserCanNotReturnNull() {
      User user = userBuilder.withEmail(TEST_EMAIL_ADDRESS).buildUser();

      when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(user));

      assertNotNull(userService.getUser(TEST_EMAIL_ADDRESS));
   }

   @Test
   @DisplayName("Method getUser() should return user with matching email address to argument email address")
   void returnUserEmailMustMatchGetEmailInArgument() {
      User user = userBuilder.withEmail(TEST_EMAIL_ADDRESS).buildUser();

      when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(user));
      UserDTO resultUser = userService.getUser(TEST_EMAIL_ADDRESS);

      assertEquals(TEST_EMAIL_ADDRESS, resultUser.email());
   }

   @Test
   @DisplayName("Method getUser() should return UserDTO matching to test UserDTO")
   void returnUserObjectMustMuchTheTestUserDTOObject() {
      User testUser = userBuilder.buildUser();
      UserDTO testUserDTO = userBuilder.buildUserDTO();

      when(userRepository.findUserByEmail(testUserDTO.email())).thenReturn(Optional.of(testUser));

      assertEquals(testUserDTO, userService.getUser(testUserDTO.email()));
   }

   @Test
   @DisplayName("Method getUser() should throw UserNotFoundException if user with provided email do not exists")
   void getUserMethodShouldThrowUserNotFoundException() {
      String testEmail = "not_existing_user_email@gmail.com";

      when(userRepository.findUserByEmail(testEmail)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> userService.getUser(testEmail));
   }

   // ADD SINGLE USER TEST

   @Test
   @DisplayName("Method addUser() should not return the null value")
   void addUserShouldNotReturnNull() {
      UserDTO userDTO = userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                                   .buildUserDTO();

      assertNotNull(userService.addUser(userDTO));
   }

   @Test
   @DisplayName("Method addUser() should return the name of created user")
   void addUserShouldReturnTheGetUsernameOfCreatedUser() {
      UserDTO userDTO = userBuilder.buildUserDTO();

      assertEquals(userDTO.username(), userService.addUser(userDTO));
   }

   @Test
   @DisplayName("Method addUser() should throw user already exists exception if user with specified email already exists.")
   void addUserShouldThrowUserAlreadyExistsException() {
      UserDTO userDTO = userBuilder.buildUserDTO();

      when(userRepository.findUserByEmail(userDTO.email())).thenReturn(Optional.of(new User()));

      assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(userDTO));
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
              userBuilder.buildUserDTO(),
              userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                         .withUsername(TEST_USERNAME)
                         .buildUserDTO()
      );

      assertEquals(userDTOs.size(), userService.addUsers(userDTOs).size());
   }

   @Test
   @DisplayName("Method addUsers() should return String List with usernames of added user")
   void addUsersShouldReturnGetUsernameList() {
      List<UserDTO> userDTOs = List.of(
              userBuilder.buildUserDTO(),
              userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                         .withUsername(TEST_USERNAME)
                         .buildUserDTO()
      );

      List<String> usernames = userService.addUsers(userDTOs);

      for (int i = 0; i < userDTOs.size(); i++) {
         assertEquals(userDTOs.get(i).username(), usernames.get(i));
      }
   }

  @Test
  @DisplayName("Method addUsers() should add only unique users from argument List")
  void addUsersShouldOnlyAddUniqueUsersToDatabase() {
      List<UserDTO> notUniqueUserDTOs = List.of(
            userBuilder.withEmail(TEST_EMAIL_ADDRESS).buildUserDTO(),
            userBuilder.withEmail(TEST_EMAIL_ADDRESS).buildUserDTO()
      );

      when(habitRepository.findHabitByName(TEST_EMAIL_ADDRESS)).thenReturn(Optional.empty());

      assertEquals(1, userService.addUsers(notUniqueUserDTOs).size());
  }

   // UPDATE USER TESTS

   @Test
   @DisplayName("Method updateUser() should not return null value")
   void updateUserShouldNotReturnNullValue() {
      when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(new User()));

      assertNotNull(userService.updateUser(TEST_EMAIL_ADDRESS, userBuilder.buildUserDTO()));
   }

   @Test
   @DisplayName("Method updateUser() should return UserDTO that matches updated object")
   void updateUserShouldReturnUserDTOObjectWhichWillMuchUpdateObject() {
      UserDTO user = userBuilder.buildUserDTO();

      when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(new User()));

      assertEquals(user, userService.updateUser(TEST_EMAIL_ADDRESS, user));
   }

   @Test
   @DisplayName("Method updateUser() should throw new UserAlreadyExistsException if user tries to updated email to already existing one")
   void updateUserShouldThrowUserAlreadyExistsExceptionIfGetEmailOfUpdatedUserIsAlreadyInDatabase() {
      UserDTO userToUpdate = userBuilder.withEmail(TEST_EMAIL_ADDRESS)
                                        .buildUserDTO();
      String tmpEmailAddress = "otherTestEmail@gmail.com";

      when(userRepository.findUserByEmail(tmpEmailAddress)).thenReturn(Optional.of(new User()));
      when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.of(new User()));

      assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(tmpEmailAddress, userToUpdate));
   }

   @Test
   @DisplayName("Method updateUser() throw new UserNotFoundException if User with specific email address doesn't exist")
   void updateUserShouldThrowUserNotFoundException() {
      UserDTO user = userBuilder.buildUserDTO();

      when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> userService.updateUser(TEST_EMAIL_ADDRESS, user));

   }

   // DELETE USER TEST

   @Test
   @DisplayName("Method deleteUser() should throw UserNotFoundException if there is no user for provided email.")
   void deleteUserShouldThrowUserNotFoundException() {
      when(userRepository.findUserByEmail(TEST_EMAIL_ADDRESS)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> userService.deleteUser(TEST_EMAIL_ADDRESS));
   }





}
