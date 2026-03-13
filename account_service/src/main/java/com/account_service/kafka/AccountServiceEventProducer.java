package com.account_service.kafka;

import com.baas.constants.account_service.AccountServiceEventType;
import com.baas.events.account_service.AccountServiceEventDto;
import com.baas.topics.KafkaTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountServiceEventProducer {
    private KafkaTemplate<String, AccountServiceEventDto> kafkaTemplate;
    private static final String TOPIC = KafkaTopics.ACCOUNT_SERVICE_EVENT;

    @Autowired
    public AccountServiceEventProducer(KafkaTemplate<String, AccountServiceEventDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void openAccountEvent(String userId, Map<String, Object> metadata){

        AccountServiceEventDto accountServiceEventDto = AccountServiceEventDto.builder()
                .accountServiceEventType(AccountServiceEventType.OPEN_ACCOUNT)
                .userId(userId)
                .message("User Account Opened Successfully!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,accountServiceEventDto);
    }

    public void closeAccountEvent(String userId, Map<String, Object> metadata){

        AccountServiceEventDto accountServiceEventDto = AccountServiceEventDto.builder()
                .accountServiceEventType(AccountServiceEventType.CLOSE_ACCOUNT)
                .userId(userId)
                .message("User Account Closed Successfully!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,accountServiceEventDto);
    }

    public void closeAllAccountEvent(String userId, Map<String, Object> metadata){
        AccountServiceEventDto accountServiceEventDto = AccountServiceEventDto.builder()
                .accountServiceEventType(AccountServiceEventType.CLOSE_ALL_ACCOUNT)
                .userId(userId)
                .message("User All Accounts Closed Successfully!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,accountServiceEventDto);
    }

    public void accountFreezeStatusEvent(String userId, Map<String, Object> metadata){

        AccountServiceEventDto accountServiceEventDto = AccountServiceEventDto.builder()
                .accountServiceEventType(AccountServiceEventType.ACCOUNT_FREEZE_UNFREEZE)
                .userId(userId)
                .message(String.format("Account: %s", metadata.get("accountFreezeStatus").toString()))
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,accountServiceEventDto);
    }

    public void updateAccountBalanceEvent(String userId, Map<String, Object> metadata){
        AccountServiceEventDto accountServiceEventDto = AccountServiceEventDto.builder()
                .accountServiceEventType(AccountServiceEventType.UPDATE_ACCOUNT_BALANCE)
                .userId(userId)
                .message("User Account Balanced Updated Successfully!")
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,accountServiceEventDto);
    }

}
