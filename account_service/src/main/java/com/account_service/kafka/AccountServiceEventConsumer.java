package com.account_service.kafka;

import com.account_service.entity.ProcessedEvents;
import com.account_service.service.ProcessedEventsService;
import com.baas.events.transaction_service.CreditReceiverAccountPayload;
import com.baas.events.transaction_service.TransactionServiceEventDto;
import com.baas.topics.KafkaTopics;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceEventConsumer {
    private final EventHandlers eventHandlers;
    private final ObjectMapper objectMapper;
    private final ProcessedEventsService processedEventsService;

    @Autowired
    public AccountServiceEventConsumer(EventHandlers eventHandlers, ObjectMapper objectMapper,ProcessedEventsService processedEventsService){
        this.eventHandlers=eventHandlers;
        this.objectMapper=objectMapper;
        this.processedEventsService=processedEventsService;

    }

    // accountId, receiverUserId, amount
    @KafkaListener(
        topics = KafkaTopics.TRANSACTION_SERVICE_EVENT,
        groupId = "transaction-kafkaService"
    )
    public void accountServiceEvent(TransactionServiceEventDto<?> transactionServiceEventDto){

        log.info("AccountServiceEventConsumer: accountServiceEvent checking if events exists");

        if(processedEventsService.checkEventExists(transactionServiceEventDto.getEventId())){
            log.info("AccountServiceEventConsumer: accountServiceEvent -> Event already processed, eventId: {}",
                    transactionServiceEventDto.getEventId());

            return;
        }

//        check for processed eventId
//        transactionServiceEventDto.getEventId()

        switch (transactionServiceEventDto.getTransactionServiceEventType()){

            case UPDATE_RECEIVER_ACCOUNT:

                log.info("AccountServiceEventConsumer: AccountServiceEventConsumer updating the receiver account");

                log.info("Printing payload details: {}",transactionServiceEventDto.getPayload().toString());

                CreditReceiverAccountPayload payload = objectMapper.convertValue(
                                transactionServiceEventDto.getPayload(),
                                CreditReceiverAccountPayload.class
                );

                if(payload==null) {
                    log.error("Unknow type of payload!");
                }

                eventHandlers.creditReceiverAccountHandler(payload);

                log.info("AccountServiceEventConsumer: AccountServiceEventConsumer updated the receiver account");

                break;
            default : log.warn("TransactionEventConsumer: Unknown Event Type!");
        }

        log.info("AccountServiceEventConsumer: switch executed!");

        processedEventsService.saveProcessedEvent(
                ProcessedEvents.builder()
                        .eventId(transactionServiceEventDto.getEventId())
                        .userId(transactionServiceEventDto.getUserId())
                        .transactionId(transactionServiceEventDto.getTransactionId())
                        .build()
        );
    }
}
