package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import io.github.ciamcioo.habit_builder.repository.UserRepository;
import io.github.ciamcioo.habit_builder.service.aspect.EnableExceptionLogging;
import io.github.ciamcioo.habit_builder.service.aspect.EnableMethodCallLogging;
import io.github.ciamcioo.habit_builder.service.aspect.EnableMethodLogging;
import io.github.ciamcioo.habit_builder.service.exceptions.ConversionException;
import io.github.ciamcioo.habit_builder.service.exceptions.UserAlreadyExistsException;
import io.github.ciamcioo.habit_builder.service.exceptions.UserNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserManagementService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserManagementService.class);

    private static final String USER_NOT_FOUND      = "User with email = %s not found";
    private static final String USER_ALREADY_EXISTS = "User with email: %s already exists";

    private final UserRepository userRepository;

    @Autowired
    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @EnableMethodLogging
    public List<UserDTO> getAllUsers() {
        List<User> users =  userRepository.findAll();
        return convertUserListToUserDTOList(users);
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public UserDTO getUser(String email) {
        User persistedUser = userRepository.findUserByEmail(email)
                                           .orElseThrow(
                                                   () -> new UserNotFoundException(String.format(USER_NOT_FOUND, email))
                                           );
        return convertUserToUserDTO(persistedUser);
    }

    @Override
    @EnableMethodLogging
    public String addUser(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException(String.format(USER_ALREADY_EXISTS, userDTO.email()));
        }

        User user = convertUserDTOToUser(userDTO);
        userRepository.saveAndFlush(user);

        return user.getUsername();
    }

    @Override
    @EnableMethodLogging
    public List<String> addUsers(List<UserDTO> userDTOs) {
        Set<UserDTO> uniqueUser = new HashSet<>(userDTOs);
        List<UserDTO> userDTOList = uniqueUser.stream()
                .filter(userDTO -> userRepository.findUserByEmail(userDTO.email()).isEmpty())
                .toList();
        List<User> users = convertUserDTOListToUserList(userDTOList);

        List<String> usernames = new ArrayList<>();
        users.forEach(user -> {
            userRepository.save(user);
            usernames.add(user.getUsername());
        });
        userRepository.flush();

        return usernames;
    }

    @Override
    @EnableExceptionLogging
    @EnableMethodLogging
    public UserDTO updateUser(String email, UserDTO updatedUser) {
        User userToUpdate = userRepository.findUserByEmail(email)
                                          .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, email)));

        if (!email.equals(updatedUser.email()) &&
            userRepository.findUserByEmail(updatedUser.email()).isPresent()) {

            throw new UserAlreadyExistsException(String.format(USER_ALREADY_EXISTS, updatedUser.email()));
        }

        userToUpdate.setEmail(updatedUser.email());
        userToUpdate.setUsername(updatedUser.username());
        userToUpdate.setFirstName(updatedUser.firstName());
        userToUpdate.setLastName(updatedUser.lastName());
        userToUpdate.setAge(updatedUser.age());

        return convertUserToUserDTO(userToUpdate);
    }

    @Override
    @EnableMethodCallLogging
    @EnableExceptionLogging
    public void deleteUser(String email) {
        User userToDelete = userRepository.findUserByEmail(email)
                                          .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, email)));

        log.info("Retried object: {}", userToDelete);

        userRepository.delete(userToDelete);
        userRepository.flush();
    }



    private List<UserDTO> convertUserListToUserDTOList(List<User> users) {
        List<UserDTO> userDTOs = new ArrayList<>();

        users.forEach(user -> userDTOs.add(convertUserToUserDTO(user)));

        return Collections.unmodifiableList(userDTOs);

    }

    private UserDTO convertUserToUserDTO(User user) {
        try {
            return new UserDTO(
                    user.getEmail(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAge()
            );
        } catch(RuntimeException e) {
            throw new ConversionException(User.class.getSimpleName(), UserDTO.class.getSimpleName());
        }
    }


    private List<User> convertUserDTOListToUserList(List<UserDTO> userDTOList) {
        List<User> userList = new ArrayList<>();
        userDTOList.forEach(userDTO ->
                userList.add(
                        convertUserDTOToUser(userDTO)
                )
        );
        return Collections.unmodifiableList(userList);
    }

    private User convertUserDTOToUser(UserDTO userDTO) {
        try {
            return new User(
                    userDTO.email(),
                    userDTO.username(),
                    userDTO.firstName(),
                    userDTO.lastName(),
                    userDTO.age()
            );
        } catch(RuntimeException e) {
            throw new ConversionException(UserDTO.class.getSimpleName(), User.class.getSimpleName());
        }
    }


}
