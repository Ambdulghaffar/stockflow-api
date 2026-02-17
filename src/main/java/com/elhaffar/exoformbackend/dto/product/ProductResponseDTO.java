package com.elhaffar.exoformbackend.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Boolean active,
        Integer categoryId,
        String categoryName, // pour le nom de la catégorie
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
