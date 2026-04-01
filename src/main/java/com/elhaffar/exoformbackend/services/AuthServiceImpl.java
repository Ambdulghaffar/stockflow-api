package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.auth.AuthResponseDTO;
import com.elhaffar.exoformbackend.dto.auth.LoginRequestDTO;
import com.elhaffar.exoformbackend.dto.auth.RegisterRequestDTO;
import com.elhaffar.exoformbackend.entities.User;
import com.elhaffar.exoformbackend.enums.UserRole;
import com.elhaffar.exoformbackend.mapper.UserMapper;
import com.elhaffar.exoformbackend.repository.UserRepository;
import com.elhaffar.exoformbackend.config.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        if(userRepository.findByPhone(dto.phone()).isPresent()){
            throw new RuntimeException("Ce numéro de téléphone est déjà utilisé");
        }
        User user = userMapper.toEntityFromRegister(dto);
        user.setRole(UserRole.CLIENT);
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);

        String accessToken = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                jwtUtils.getExpirationTime(),
                user.getEmail(),
                user.getRole()
        );
    }


    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String accessToken = jwtUtils.generateToken(user.getEmail(),user.getRole().name());
        String refreshToken =  jwtUtils.generateRefreshToken(user.getEmail());

        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                jwtUtils.getExpirationTime(),
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        if(!jwtUtils.isTokenValid(refreshToken)){
            throw new RuntimeException("Refresh Token expiré ou invalide");
        }
         String email = jwtUtils.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Utilisateur non trouvé"));

        // On génère un nouvel Access Token, mais on garde le même Refresh Token (ou on en génère un nouveau)
        String newAccessToken = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponseDTO(
                newAccessToken,
                refreshToken,
                jwtUtils.getExpirationTime(),
                user.getEmail(),
                user.getRole()
        );
    }
}
