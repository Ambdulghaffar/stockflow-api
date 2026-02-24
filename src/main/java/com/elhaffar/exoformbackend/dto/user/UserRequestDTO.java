package com.elhaffar.exoformbackend.dto.user;

import com.elhaffar.exoformbackend.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        String username,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email doit être valide")
        String email,

        String phone,

        String address,

        UserRole role
) {}
