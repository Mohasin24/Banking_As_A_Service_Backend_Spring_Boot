package com.baas.events.authentication_service;

import com.baas.constants.authentication_service.AuthEventType;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthEventDto {
    private AuthEventType eventType;
    private String userId;
    private String message;
    private Map<String, Object> metadata;
}
