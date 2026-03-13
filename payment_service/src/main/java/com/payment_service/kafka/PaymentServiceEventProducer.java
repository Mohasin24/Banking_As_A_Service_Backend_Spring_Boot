package com.payment_service.kafka;

import com.baas.constants.payment_service.PaymentServiceEventType;
import com.baas.events.payment_service.PaymentServiceEventDto;
import com.baas.topics.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentServiceEventProducer {
    private final KafkaTemplate<String, PaymentServiceEventDto> kafkaTemplate;
    private final String TOPIC = KafkaTopics.PAYMENT_SERVICE_EVENT;

    public PaymentServiceEventProducer(KafkaTemplate<String, PaymentServiceEventDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void initiatePaymentEvent(String userId, Map<String,Object> metadata){

        String html = """
                <html>
                    <body>
                        <h3>Payment: %s</h3>
                        <p><strong>User Id:</strong> %d</p>
                        <p><strong>Payment Id:</strong> %d</p>
                        <p><strong>Payment Type:</strong> %s</p>
                        <p><strong>Status:</strong> %s</p> 
                        <p><strong>Failure Reason:</strong> %s</p>                                         
                    </body>
                </html>
                """;

        String body = html.formatted(
                metadata.get("paymentStatus"),
                metadata.get("userId"),
                metadata.get("paymentId"),
                metadata.get("paymentType"),
                metadata.get("paymentStatus"),
                metadata.get("failureReason")
        );


        PaymentServiceEventDto paymentServiceEventDto = PaymentServiceEventDto.builder()
                .userId(userId)
                .paymentServiceEventType(PaymentServiceEventType.INITIATE_PAYMENT)
                .message(body)
                .metadata(metadata)
                .build();

        kafkaTemplate.send(TOPIC,userId,paymentServiceEventDto);
    }
}
