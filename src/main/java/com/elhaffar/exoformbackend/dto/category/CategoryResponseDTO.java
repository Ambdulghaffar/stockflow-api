package com.elhaffar.exoformbackend.dto.category;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Integer id,
        String name,
        String description,
        String imageUrl,
        int productCount,       // nombre de produits dans la catégorie
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
