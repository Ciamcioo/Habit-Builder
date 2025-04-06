package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import io.github.ciamcioo.habit_builder.repository.UserRepository;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableExceptionLogging;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodCallLogging;
import io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodLogging;
import io.github.ciamcioo.habit_builder.exception.UserAlreadyExistsException;
import io.github.ciamcioo.habit_builder.exception.UserNotFoundException;

import io.github.ciamcioo.habit_builder.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserManagementService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserManagementService.class);

    private static final String USER_NOT_FOUND_MESSAGE_FORMAT      = "User with given email: %s not found";
    private static final String USER_ALREADY_EXISTS_MESSAGE_FORMAT = "User with given email: %s already exists";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserManagementService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @EnableMethodLogging
    public List<UserDTO> getAllUsers() {
        List<User> persistedUsers = userRepository.findAll();

        return persistedUsers.stream()
                             .map(userMapper::toDTO)
                             .toList();
    }

    @Override
    @EnableMethodLogging
    @EnableExceptionLogging
    public UserDTO getUser(String email) {
        User persistedUser = userRepository.findUserByEmail(email)
                                           .orElseThrow(
                                                   () -> new UserNotFoundException(String.format(USER_NOT_FOUND_MESSAGE_FORMAT, email))
                                           );

        return userMapper.toDTO(persistedUser);
    }

    @Override
    @EnableMethodLogging
    public String addUser(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException(String.format(USER_ALREADY_EXISTS_MESSAGE_FORMAT, userDTO.email()));
        }

        User user = userMapper.toEntity(userDTO);
        userRepository.saveAndFlush(user);

        return user.getUsername();
    }

    @Override
    @EnableMethodLogging
    public List<String> addUsers(List<UserDTO> userDTOs) {
        Set<UserDTO> uniqueUser = new HashSet<>(userDTOs);

        List<User> usersToPersist = uniqueUser.stream()
                                              .filter(userDTO -> userRepository.findUserByEmail(userDTO.email()).isEmpty())
                                              .map(userMapper::toEntity)
                                              .toList();

        userRepository.saveAllAndFlush(usersToPersist);

        return usersToPersist.stream()
                             .map(User::getUsername)
                             .toList();
    }

    @Override
    @EnableExceptionLogging
    @EnableMethodLogging
    public UserDTO updateUser(String email, UserDTO updatedUser) {
        User userToUpdate = userRepository.findUserByEmail(email)
                                          .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MESSAGE_FORMAT, email)));

        if (!email.equals(updatedUser.email()) &&
            userRepository.findUserByEmail(updatedUser.email()).isPresent()) {

            throw new UserAlreadyExistsException(String.format(USER_ALREADY_EXISTS_MESSAGE_FORMAT, updatedUser.email()));
        }

        userToUpdate.setEmail(updatedUser.email());
        userToUpdate.setUsername(updatedUser.username());
        userToUpdate.setFirstName(updatedUser.firstName());
        userToUpdate.setLastName(updatedUser.lastName());
        userToUpdate.setAge(updatedUser.age());

        return userMapper.toDTO(userToUpdate);
    }

    @Override
    @EnableMethodCallLogging
    @EnableExceptionLogging
    public void deleteUser(String email) {
        User userToDelete = userRepository.findUserByEmail(email)
                                          .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MESSAGE_FORMAT, email)));

        log.info("Retrieved object: {}", userToDelete);

        userRepository.delete(userToDelete);
        userRepository.flush();
    }
}
