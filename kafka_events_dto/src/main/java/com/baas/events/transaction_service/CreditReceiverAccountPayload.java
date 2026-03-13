package com.baas.events.transaction_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreditReceiverAccountPayload {
    private long accountId;
    private long userId;
    private String accountNo;
    private BigDecimal amount;
    private String accountType;
    private String accountStatus;
}
