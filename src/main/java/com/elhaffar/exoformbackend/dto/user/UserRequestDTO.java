package com.elhaffar.exoformbackend.dto.user;

import com.elhaffar.exoformbackend.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        String username,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email doit être valide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, message = "Au moins 8 caractères")
        String password,

        String phone,

        String address,

        UserRole role
) {}
