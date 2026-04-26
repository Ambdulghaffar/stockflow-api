package com.elhaffar.exoformbackend.dto.product;

import com.elhaffar.exoformbackend.enums.ProductStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 200, message = "Le nom doit contenir entre 2 et 200 caractères")
        String name,

        @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
        String description,

        @NotNull(message = "Le prix est obligatoire")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
        BigDecimal price,

        @NotNull(message = "Le stock est obligatoire")
        @Min(value = 0, message = "Le stock ne peut pas être négatif")
        Integer stock,

        String imageUrl,

        @NotNull(message = "La catégorie est obligatoire")
        Integer categoryId,

        @NotNull(message = "Le statut est obligatoire")
        ProductStatus status
) {}
