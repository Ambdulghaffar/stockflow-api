package com.elhaffar.exoformbackend.config;

import com.elhaffar.exoformbackend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permet d'utiliser @PreAuthorize("hasRole('ADMIN')") sur tes controllers
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // Injection par constructeur manuel (Style Pro)
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Hachage sécurisé BCrypt
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactivé car nous sommes en Stateless (JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de session côté serveur
                .authorizeHttpRequests(auth -> auth
                        // 1. Endpoints publics (Login & Register)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. Consultation libre (Optionnel : tout le monde peut voir les produits)
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // 3. Exemple de protection par rôle (Optionnel si tu utilises @PreAuthorize)
                        .requestMatchers("/api/users/**").hasAuthority("ROLE_ADMIN")

                        // 4. Tout le reste nécessite un Token JWT valide
                        .anyRequest().authenticated()
                );

        // Ajout du filtre JWT AVANT le filtre d'authentification standard de Spring
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
