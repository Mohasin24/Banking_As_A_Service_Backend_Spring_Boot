package com.transaction_service.dto;

import com.transaction_service.constant.TransactionStatus;
import com.transaction_service.constant.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferResponse(String transactionId, String failureReason,TransactionStatus transactionStatus) {
}
