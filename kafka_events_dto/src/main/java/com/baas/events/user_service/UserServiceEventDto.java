package com.baas.events.user_service;

import com.baas.constants.user_service.UserServiceEventType;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserServiceEventDto
{
    private UserServiceEventType userServiceEventType;
    private String userId;
    private String message;
    private Map<String, Object> metadata;
}
