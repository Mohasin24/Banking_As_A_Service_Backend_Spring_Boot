package com.account_service.dto;

import com.account_service.constant.AccountStatus;
import com.account_service.constant.AccountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record AccountResponse(
        long accountId,
        long userId,
        String accountNo,
        AccountType accountType,
        AccountStatus accountStatus,
        BigDecimal accountBalance,
        Instant createdAt,
        Instant updatedAt) {
}
