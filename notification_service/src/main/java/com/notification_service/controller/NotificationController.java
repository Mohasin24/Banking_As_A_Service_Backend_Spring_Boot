package com.notification_service.controller;

import com.notification_service.kafka_producer.NotificationEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController
{
    private final NotificationEventProducer notificationEventProducer;

    @Autowired
    public NotificationController(NotificationEventProducer notificationEventProducer){
        this.notificationEventProducer=notificationEventProducer;
    }

    @GetMapping("/test")
    public String test(){
        notificationEventProducer.testKafka("Testing sendgrid email service notification");
        return "NotificationController controller";
    }
}