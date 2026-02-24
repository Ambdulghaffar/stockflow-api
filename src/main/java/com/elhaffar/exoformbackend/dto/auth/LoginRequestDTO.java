package com.elhaffar.exoformbackend.dto.auth;

public record LoginRequestDTO(
        String email, String password
) {}
