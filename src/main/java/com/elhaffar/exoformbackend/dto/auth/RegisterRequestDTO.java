package com.elhaffar.exoformbackend.dto.auth;

import com.elhaffar.exoformbackend.enums.UserRole;

public record RegisterRequestDTO(
        String username,
        String email,
        String phone,
        String address,
        UserRole role,
        String password
) {}