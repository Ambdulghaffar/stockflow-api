package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.auth.AuthResponseDTO;
import com.elhaffar.exoformbackend.dto.auth.LoginRequestDTO;
import com.elhaffar.exoformbackend.dto.auth.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO dto);
    AuthResponseDTO login(LoginRequestDTO dto);
    AuthResponseDTO refreshToken (String refreshToken);
}
