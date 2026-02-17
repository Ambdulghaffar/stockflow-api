package com.elhaffar.exoformbackend.dto.product;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 3, message = "Le nom doit comporter au moins 3 caractères")
        String name,

        String description,

        @Positive(message = "Le prix doit être supérieur à 0")
        BigDecimal price,

        @Min(value = 0, message = "La quantité ne peut pas être négative")
        Integer quantity,

        @NotNull(message = "L'ID de la catégorie est obligatoire")
        Integer categoryId
) {}
