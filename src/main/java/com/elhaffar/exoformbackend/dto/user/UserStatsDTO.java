package com.elhaffar.exoformbackend.dto.user;

public record UserStatsDTO(
        long total,
        long admins,
        long managers,
        long clients
) {}
