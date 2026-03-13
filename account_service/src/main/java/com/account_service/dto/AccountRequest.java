package com.account_service.dto;

import lombok.Builder;

@Builder
public record AccountRequest(
        long userId,
        String accountType) {
}
