package com.user_service.kafka;

import com.baas.constants.user_service.UserServiceEventType;
import com.baas.events.user_service.UserServiceEventDto;
import com.baas.topics.KafkaTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceEventProducer {
    private final KafkaTemplate<String, UserServiceEventDto> kafkaTemplate;
    private final String TOPIC = KafkaTopics.USER_SERVICE_EVENT;

    @Autowired
    public UserServiceEventProducer(KafkaTemplate<String, UserServiceEventDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void userProfileUpdateEvent(String userId, Map<String, Object> metadata){

        String html = """
                <html>
                    <body>
                        <h2>Hello, %s</h2>
                        <p>Your user profile has been updated successfully!</p>
                    </body>
                </html>
                """;
        String body = html.formatted(metadata.get("fullName"));

        UserServiceEventDto userServiceEventDto = UserServiceEventDto.builder()
                .userServiceEventType(UserServiceEventType.UPDATE_PROFILE)
                .userId(userId)
                .message(body)
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,userServiceEventDto);
    }

    public void kycStatusUpdateEvent(String userId, Map<String, Object> metadata){
        String html = """
                <html>
                    <body>
                        <h2>Hello, %s</h2>
                        <p>Kyc Status for your account has been updated successfully!</p>
                        <p>Updated KYC Status: %s</p>
                    </body>
                </html>
                """;

        String body = html.formatted(
                metadata.get("fullName"),
                metadata.get("kycStatus")
        );

        UserServiceEventDto userServiceEventDto = UserServiceEventDto.builder()
                .userServiceEventType(UserServiceEventType.UPDATE_KYC_STATUS)
                .userId(userId)
                .message(body)
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,userServiceEventDto);
    }

    public void userProfileDeleteEvent(String userId, Map<String, Object> metadata){
        String html = """
                <html>
                    <body>
                        <h2>Hello, %s</h2>
                        <p>%s</p>
                    </body>
                </html>
                """;
        String body = html.formatted(
                metadata.get("fullName"),
                metadata.get("message")
        );

        UserServiceEventDto userServiceEventDto = UserServiceEventDto.builder()
                .userServiceEventType(UserServiceEventType.USER_DELETED)
                .userId(userId)
                .message(body)
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,userServiceEventDto);
    }
}
