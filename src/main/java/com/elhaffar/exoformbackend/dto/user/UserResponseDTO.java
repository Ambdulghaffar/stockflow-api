package com.elhaffar.exoformbackend.dto.user;

import com.elhaffar.exoformbackend.enums.UserRole;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Integer id,
        String keycloakId,
        String username,
        String email,
        String phone,
        String address,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
