package com.elhaffar.exoformbackend.dto.category;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Integer id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
