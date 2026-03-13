package com.baas.events.transaction_service;

import com.baas.constants.transaction_service.TransactionServiceEventType;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionServiceEventDto<T> {

    //Unique identifier for each event request, using UUID
    private String eventId;

    private String userId;
    private TransactionServiceEventType transactionServiceEventType;
    private String transactionId;
    private String message;
    private T payload;
    private Map<String, Object> metadata;
}
