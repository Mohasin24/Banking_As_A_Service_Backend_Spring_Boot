package com.transaction_service.dto;

import com.transaction_service.constant.TransactionStatus;
import com.transaction_service.constant.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        String transactionId,
        long userId,
        long fromAccountId,
        long toAccountId,
        BigDecimal amount,
        String transactionType,
        String description,
        LocalDateTime transactionDate,
        String failureReason,
        TransactionStatus transactionStatus
) {
}
