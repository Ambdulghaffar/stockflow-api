package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.user.UserRequestDTO;
import com.elhaffar.exoformbackend.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO);
    void deleteUser(Integer id);
    UserResponseDTO getUserById(Integer id);
}
