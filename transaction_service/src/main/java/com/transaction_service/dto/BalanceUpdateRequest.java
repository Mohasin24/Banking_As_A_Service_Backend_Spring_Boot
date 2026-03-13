package com.transaction_service.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceUpdateRequest(BigDecimal amount) {
}
