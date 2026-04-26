package com.elhaffar.exoformbackend.dto.product;

import com.elhaffar.exoformbackend.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl,
        Integer categoryId,
        String categoryName,
        ProductStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
