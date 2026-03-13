package com.payment_service.dto;

import com.payment_service.constant.PaymentType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(
        long userId,
        long fromAccountId,
        long toAccountId,
        PaymentType paymentType,
        BigDecimal amount
)
{ }
