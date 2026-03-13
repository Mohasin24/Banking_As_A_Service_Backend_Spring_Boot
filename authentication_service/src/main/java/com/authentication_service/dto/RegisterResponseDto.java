package com.authentication_service.dto;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponseDto(
        String username,
        String email,
        String role,
        String status,
        Instant createdAt,
        Instant updatedAt) {
}
