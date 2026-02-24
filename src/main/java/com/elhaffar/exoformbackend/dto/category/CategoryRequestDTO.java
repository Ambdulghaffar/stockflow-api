package com.elhaffar.exoformbackend.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 3, message = "Le nom doit comporter au moins 3 caractères")
        String name,

        String description
) {}
