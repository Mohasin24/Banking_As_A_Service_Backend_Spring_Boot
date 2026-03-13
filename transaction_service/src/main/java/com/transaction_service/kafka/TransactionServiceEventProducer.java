package com.transaction_service.kafka;

import com.baas.constants.transaction_service.TransactionServiceEventType;
import com.baas.events.transaction_service.CreditReceiverAccountPayload;
import com.baas.events.transaction_service.TransactionServiceEventDto;
import com.baas.topics.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class TransactionServiceEventProducer {

    private final KafkaTemplate<String, TransactionServiceEventDto> kafkaTemplate;
    private final String TOPIC = KafkaTopics.TRANSACTION_SERVICE_EVENT;
    private final UUID uuid = UUID.randomUUID();
    @Autowired
    public TransactionServiceEventProducer(KafkaTemplate<String,TransactionServiceEventDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void depositEvent(String userId, Map<String, Object> metadata){

        String html = """
                <html>
                    <body>
                        <h2>Account Deposited!</h2>
                        <p><strong>User Id:</strong> %d </p>
                        <p><strong>Transaction Id:</strong> %s </p>
                        <p><strong>Amount:</strong> %d </p>
                        <p><strong>Transaction Type:</strong> %s </p>
                        <p><strong>Description:</strong> %s </p>
                        <p><strong>Transaction Date:</strong> %s </p>
                        <p><strong>Failure Reason:</strong> %s </p>
                        <p><strong>Transaction Status:</strong> %s </p>
                    </body>
                </html>
                """;

        String body = html.formatted(
                metadata.get("userId"),
                metadata.get("transactionId"),
                metadata.get("amount"),
                metadata.get("transactionType"),
                metadata.get("description"),
                metadata.get("transactionDate"),
                metadata.get("failureReason"),
                metadata.get("transactionStatus")
        );

        TransactionServiceEventDto<Void> transactionServiceEventDto = TransactionServiceEventDto.<Void>builder()
                .userId(userId)
                .message(body)
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,transactionServiceEventDto);
    }

    public void withdrawEvent(String userId, Map<String, Object> metadata){

        String html = """
                <html>
                    <body>
                        <h2>Account Withdrawal!</h2>
                        <p><strong>User Id:</strong> %d </p>
                        <p><strong>Transaction Id:</strong> %s </p>
                        <p><strong>Amount:</strong> %d </p>
                        <p><strong>Transaction Type:</strong> %s </p>
                        <p><strong>Description:</strong> %s </p>
                        <p><strong>Transaction Date:</strong> %t </p>
                        <p><strong>Failure Reason:</strong> %s </p>
                        <p><strong>Transaction Status:</strong> %s </p>
                    </body>
                </html>
                """;

        String body = html.formatted(
                metadata.get("userId"),
                metadata.get("transactionId"),
                metadata.get("description"),
                metadata.get("transactionType"),
                metadata.get("transactionDate"),
                metadata.get("failureReason"),
                metadata.get("amount"),
                metadata.get("transactionStatus")
        );

        TransactionServiceEventDto<Void> transactionServiceEventDto = TransactionServiceEventDto.<Void>builder()
                .userId(userId)
                .message(body)
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,transactionServiceEventDto);
    }

    public void updateReceiverAccountEvent(String userId,String transactionId, Map<String, Object> metadata, CreditReceiverAccountPayload creditReceiverAccountPayload){
        log.info("TransactionServiceEventProducer: updateReceiverAccountEvent -> CreditReceiverAccountPayload details: {}",creditReceiverAccountPayload.toString());
        TransactionServiceEventDto<CreditReceiverAccountPayload> transactionServiceEventDto =
                TransactionServiceEventDto.<CreditReceiverAccountPayload>builder()
                        .eventId(uuid.toString())
                        .userId(userId)
                        .transactionServiceEventType(TransactionServiceEventType.UPDATE_RECEIVER_ACCOUNT)
                        .transactionId(transactionId)
                        .message("Receiver Account Balance Update!")
                        .metadata(metadata)
                        .payload(creditReceiverAccountPayload)
                        .build();
        
        kafkaTemplate.send(TOPIC,transactionServiceEventDto);

        log.info("TransactionServiceEventProducer: updateReceiverAccountEvent-> sent the event successfully!");
    }
}
