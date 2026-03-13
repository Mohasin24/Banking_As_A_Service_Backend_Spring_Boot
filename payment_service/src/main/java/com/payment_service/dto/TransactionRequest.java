package com.payment_service.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionRequest(long userId,long fromAccountId, long toAccountId, BigDecimal amount) {
}
