package com.elhaffar.exoformbackend.dto.user;

import com.elhaffar.exoformbackend.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDTO(
        @NotBlank(message = "Le nom est obligatoire")
        String username,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Email invalide")
        String email,

        String phone,
        String address,

        @NotNull(message = "Le rôle est obligatoire")
        UserRole role
) {}
