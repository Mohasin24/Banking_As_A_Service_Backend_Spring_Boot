package com.authentication_service.dto;

public record LoginResponseDto(
        String username,
        String email,
        String role,
        String status,
        String token) {
}
