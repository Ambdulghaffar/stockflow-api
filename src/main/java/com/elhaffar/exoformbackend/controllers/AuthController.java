package com.elhaffar.exoformbackend.controllers;

import com.elhaffar.exoformbackend.dto.auth.*;
import com.elhaffar.exoformbackend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequestDTO dto) {
        authService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@RequestBody RefreshRequestDTO request) {
        return authService.refreshToken(request.refreshToken());
    }
}
