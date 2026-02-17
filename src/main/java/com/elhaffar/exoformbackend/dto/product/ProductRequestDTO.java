package com.elhaffar.exoformbackend.dto.product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Le nom est obligatoire")
        @Min(value = 3, message = "Le nom doit comporter au moins 3 caractères")
        String name,

        String description,

        @Positive(message = "Le prix doit être supérieur à 0")
        BigDecimal price,

        @Min(value = 0, message = "La quantité ne peut pas être négative")
        Integer quantity,

        @NotNull(message = "L'ID de la catégorie est obligatoire")
        Integer categoryId
) {}
