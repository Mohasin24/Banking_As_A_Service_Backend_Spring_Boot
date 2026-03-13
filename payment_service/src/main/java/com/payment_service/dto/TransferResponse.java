package com.payment_service.dto;

import com.payment_service.constant.TransactionStatus;
import lombok.Builder;

@Builder
public record TransferResponse(String transactionId, String failureReason, TransactionStatus transactionStatus) {
}
