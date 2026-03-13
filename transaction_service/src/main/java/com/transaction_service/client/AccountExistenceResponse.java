package com.transaction_service.client;

import lombok.Builder;

@Builder
public record AccountExistenceResponse(
        long accountId,
        long userId,
        String accountNo,
        String accountType,
        String accountStatus
) {
}
