package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.dto.user.UserRequestDTO;
import com.elhaffar.exoformbackend.dto.user.UserResponseDTO;

public interface UserService {
    PageResponseDTO<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir );
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO);
    void deleteUser(Integer id);
    UserResponseDTO getUserById(Integer id);
}
