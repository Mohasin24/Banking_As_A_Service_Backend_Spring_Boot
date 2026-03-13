package com.notification_service.kafka_producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationEventProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;

    private static final String TOPIC = "test-event";

    @Autowired
    public NotificationEventProducer(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void testSendgrid(String msg){
        kafkaTemplate.send(TOPIC,"Testing the sendgrid email service");
        System.err.println("Test notification sent");
    }

    public void testKafka(String msg){
        kafkaTemplate.send(TOPIC,msg);
        log.info("Testing KafKa");
    }

}
