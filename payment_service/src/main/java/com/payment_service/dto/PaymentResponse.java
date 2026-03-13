package com.payment_service.dto;

import com.payment_service.constant.PaymentStatus;
import com.payment_service.constant.PaymentType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        long paymentId,
        String transactionId,
        long userId,
        long fromAccountId,
        long toAccountId,
        PaymentType paymentType,
        BigDecimal amount,
        PaymentStatus paymentStatus,
        LocalDateTime paymentDate,
        String paymentFailureReason
)
{ }
