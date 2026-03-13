package com.transaction_service.dto;

import com.transaction_service.constant.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionRequest(long userId,long fromAccountId, long toAccountId, BigDecimal amount) {
}
