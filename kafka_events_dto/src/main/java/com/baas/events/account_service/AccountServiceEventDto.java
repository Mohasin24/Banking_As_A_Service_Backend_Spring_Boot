package com.baas.events.account_service;

import com.baas.constants.account_service.AccountServiceEventType;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountServiceEventDto {
    private AccountServiceEventType accountServiceEventType;
    private String userId;
    private String message;
    private Map<String, Object> metadata;
}
