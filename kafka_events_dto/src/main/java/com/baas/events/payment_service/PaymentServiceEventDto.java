package com.baas.events.payment_service;

import com.baas.constants.payment_service.PaymentServiceEventType;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentServiceEventDto {
    private PaymentServiceEventType paymentServiceEventType;
    private String userId;
    private String message;
    private Map<String, Object> metadata;
}
