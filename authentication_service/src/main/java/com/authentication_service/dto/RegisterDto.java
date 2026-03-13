package com.authentication_service.dto;

public record RegisterDto(
        String username,
        String email,
        String password,
        String role) {
}
