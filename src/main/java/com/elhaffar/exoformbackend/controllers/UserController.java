package com.elhaffar.exoformbackend.controllers;

import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.dto.user.UserRequestDTO;
import com.elhaffar.exoformbackend.dto.user.UserResponseDTO;
import com.elhaffar.exoformbackend.dto.user.UserStatsDTO;
import com.elhaffar.exoformbackend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size,
            @RequestParam(defaultValue = "id")   String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false)      String role  //optionnel
    ) {
        return ResponseEntity.ok(
                userService.getAllUsers(page, size, sortBy, sortDir, role)
        );
    }

    // 201 Created
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequestDTO));
    }

    // 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }

    // 204 No Content au lieu de void avec 200
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<UserStatsDTO> getUserStats() {
        return ResponseEntity.ok(userService.getUserStats());
    }
}