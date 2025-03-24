package io.github.ciamcioo.habit_builder.service;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUser(String email);

    String addUser(UserDTO userDTO);

    List<String> addUsers(List<UserDTO> userDTOList);

    UserDTO updateUser(String email, UserDTO updatedUser);

    void deleteUser(String email);


}
