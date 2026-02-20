package com.elhaffar.exoformbackend.mapper;

import com.elhaffar.exoformbackend.dto.user.UserRequestDTO;
import com.elhaffar.exoformbackend.dto.user.UserResponseDTO;
import com.elhaffar.exoformbackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // 1. Transformation Unique (Détails d'un utilisateur)
    UserResponseDTO toResponseDTO(User user);

    // 2. Transformation de Liste (Pour le "Afficher tout")
    List<UserResponseDTO> toResponseDTOList(List<User> users);

    // 3. Création (DTO -> Nouvelle Entité)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequestDTO dto);

    /**
     * 4. Mise à jour (DTO -> Entité existante)
     * Met à jour l'entité existante avec les données du DTO
     * @MappingTarget indique à MapStruct de modifier l'objet existant
     * au lieu d'en créer un nouveau.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDto(UserRequestDTO dto, @MappingTarget User user);

}
