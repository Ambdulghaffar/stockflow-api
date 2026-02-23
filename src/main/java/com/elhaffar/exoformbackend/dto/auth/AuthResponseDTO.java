package com.elhaffar.exoformbackend.dto.auth;

import com.elhaffar.exoformbackend.enums.UserRole;

public record AuthResponseDTO(
        String token,
        String email,
        UserRole role
) {}
