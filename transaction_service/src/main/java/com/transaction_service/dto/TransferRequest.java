package com.transaction_service.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferRequest(long userId,long fromAccountId, long toAccountId, BigDecimal amount) {
}
