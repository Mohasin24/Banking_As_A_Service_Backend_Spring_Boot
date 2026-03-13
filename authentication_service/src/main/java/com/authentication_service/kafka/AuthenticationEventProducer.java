package com.authentication_service.kafka;


import com.baas.events.authentication_service.AuthEventDto;
import com.baas.constants.authentication_service.AuthEventType;
import com.baas.topics.KafkaTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationEventProducer {

    private final KafkaTemplate<String, AuthEventDto> kafkaTemplate;
    private static final String TOPIC = KafkaTopics.AUTHENTICATION_SERVICE_EVENT;

    @Autowired
    public AuthenticationEventProducer(KafkaTemplate<String, AuthEventDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void newUserRegistrationEvent(String userId, Map<String, Object> metadata){

        AuthEventDto authEventDto = AuthEventDto.builder()
                .eventType(AuthEventType.NEW_USER_REGISTRATION)
                .userId(userId)
                .message("User Registration Successful!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,authEventDto);
    }

    public void userLoginEvent(String userId, Map<String, Object> metadata){

        AuthEventDto authEventDto = AuthEventDto.builder()
                .eventType(AuthEventType.USER_LOGGED_IN)
                .userId(userId)
                .message("User Login Successful!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,authEventDto);
    }

    public void userEmailUpdateEvent(String userId, Map<String, Object> metadata){
        AuthEventDto authEventDto = AuthEventDto.builder()
                .eventType(AuthEventType.USER_EMAIL_UPDATE)
                .userId(userId)
                .message("Email updated successfully!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId, authEventDto);
    }

    public void updateUserPasswordEvent(String userId, Map<String, Object> metadata){
        AuthEventDto authEventDto = AuthEventDto.builder()
                .eventType(AuthEventType.USER_PASSWORD_UPDATE)
                .userId(userId)
                .message("Password updated successfully!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId, authEventDto);
    }
}
