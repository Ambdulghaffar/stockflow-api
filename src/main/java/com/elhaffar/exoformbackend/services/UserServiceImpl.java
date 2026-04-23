package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.dto.user.UserRequestDTO;
import com.elhaffar.exoformbackend.dto.user.UserResponseDTO;
import com.elhaffar.exoformbackend.entities.User;
import com.elhaffar.exoformbackend.enums.UserRole;
import com.elhaffar.exoformbackend.exceptions.BusinessException;
import com.elhaffar.exoformbackend.exceptions.ResourceNotFoundException;
import com.elhaffar.exoformbackend.mapper.UserMapper;
import com.elhaffar.exoformbackend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public PageResponseDTO<UserResponseDTO> getAllUsers(
            int page, int size, String sortBy, String sortDir, String role) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> result;

        if (role != null && !role.isBlank() && !role.equalsIgnoreCase("all")) {
            try {
                // Convertit le String "ADMIN" → UserRole.ADMIN
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                result = userRepository.findByRole(userRole, pageable);
            } catch (IllegalArgumentException e) {
                // Si le rôle n'existe pas dans l'enum → retourne tout
                result = userRepository.findAll(pageable);
            }
        } else {
            result = userRepository.findAll(pageable);
        }

        return PageResponseDTO.from(result.map(userMapper::toResponseDTO));
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        // BusinessException au lieu de RuntimeException → 409 Conflict
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new BusinessException("Cet email est déjà utilisé");
        }
        if (userRepository.findByPhone(dto.phone()).isPresent()) {
            throw new BusinessException("Ce numéro de téléphone est déjà utilisé");
        }

        User saved = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toResponseDTO(saved);
    }

    @Override
    public UserResponseDTO updateUser(Integer id, UserRequestDTO dto) {
        // ResourceNotFoundException → 404
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));

        // Vérifie les doublons en excluant l'utilisateur courant
        userRepository.findByEmail(dto.email())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new BusinessException("Cet email est déjà utilisé"); });

        userRepository.findByPhone(dto.phone())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new BusinessException("Ce numéro de téléphone est déjà utilisé"); });

        userMapper.updateUserFromDto(dto, existing);
        return userMapper.toResponseDTO(userRepository.save(existing));
    }

    @Override
    public void deleteUser(Integer id) {
        // ResourceNotFoundException → 404
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO getUserById(Integer id) {
        // ResourceNotFoundException → 404
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
    }
}