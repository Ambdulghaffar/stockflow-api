package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.user.UserRequestDTO;
import com.elhaffar.exoformbackend.dto.user.UserResponseDTO;
import com.elhaffar.exoformbackend.entities.User;
import com.elhaffar.exoformbackend.mapper.UserMapper;
import com.elhaffar.exoformbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAllByOrderByIdDesc();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }
        if(userRepository.findByPhone(userRequestDTO.phone()).isPresent()){
            throw new RuntimeException("Ce numéro de téléphone existe déjà ");
        }
        // Mapper le DTO en Entité User
        User userToSave = userMapper.toEntity(userRequestDTO);

        // Sauvegarder dans la DB
        User savedUser = userRepository.save(userToSave);

        // Retourner le ResponseDTO
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO) {
        // Récupérer l'utilisateur existant
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));

        userRepository.findByEmail(userRequestDTO.email()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new RuntimeException("Cet email est déjà utilisé !");
            }
        });
        userRepository.findByPhone(userRequestDTO.phone()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new RuntimeException("Ce numéro de téléphone est déjà utilisé !");
            }
        });

        // Mettre à jour les champs de l'entité existante avec les données du DTO
        userMapper.updateUserFromDto(userRequestDTO, existingUser);

        // Sauvegarder les modifications dans la DB
        User updatedUser = userRepository.save(existingUser);

        // Retourner le ResponseDTO mis à jour
        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
        return userMapper.toResponseDTO(user);
    }
}
