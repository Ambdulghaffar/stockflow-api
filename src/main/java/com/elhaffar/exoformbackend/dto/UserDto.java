package com.elhaffar.exoformbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}
