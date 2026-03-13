package com.notification_service.kafka_consumer;

import com.baas.events.account_service.AccountServiceEventDto;
import com.baas.events.authentication_service.AuthEventDto;
import com.baas.events.loan_service.LoanServiceEventDto;
import com.baas.events.payment_service.PaymentServiceEventDto;
import com.baas.events.transaction_service.TransactionServiceEventDto;
import com.baas.events.user_service.UserServiceEventDto;
import com.baas.topics.KafkaTopics;
import com.notification_service.service.SendGridEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationEventConsumer {

    @Value("${twilio.sendgrid.from-email}")
    private String toEmail;
    private final SendGridEmailService sendGridEmailService;


    @Autowired
    public NotificationEventConsumer(SendGridEmailService sendGridEmailService){
        this.sendGridEmailService=sendGridEmailService;
    }

    @KafkaListener(
            topics = KafkaTopics.AUTHENTICATION_SERVICE_EVENT,
            groupId = "authentication-kafkaService"
    )
    public void authenticationServiceEventConsumer(AuthEventDto authEventDto){

        switch (authEventDto.getEventType()){
            case USER_LOGGED_IN -> sendGridEmailService.sendSingleEmail(toEmail,"Login Alert!",authEventDto.getMessage());
            case USER_EMAIL_UPDATE -> sendGridEmailService.sendSingleEmail(toEmail,"Email Update Alert!",authEventDto.getMessage());
            case USER_PASSWORD_UPDATE -> sendGridEmailService.sendSingleEmail(toEmail,"Password Update Alert!",
                    authEventDto.getMessage());
            case NEW_USER_REGISTRATION -> sendGridEmailService.sendSingleEmail(toEmail, "New User Registration!",
                    authEventDto.getMessage());
            default -> log.warn("AuthenticationServiceEventConsumer: Unknown event type!");
        }

    }

    @KafkaListener(
            topics = KafkaTopics.ACCOUNT_SERVICE_EVENT,
            groupId = "account-kafkaService"
    )
    public void accountServiceEventConsumer(AccountServiceEventDto accountServiceEventDto){
        switch (accountServiceEventDto.getAccountServiceEventType()){
            case OPEN_ACCOUNT -> sendGridEmailService.sendSingleEmail(toEmail, "New Account Opened!",accountServiceEventDto.getMessage());
            case CLOSE_ACCOUNT -> sendGridEmailService.sendSingleEmail(toEmail,"Account Closed!",accountServiceEventDto.getMessage());
            case CLOSE_ALL_ACCOUNT -> sendGridEmailService.sendSingleEmail(toEmail,"All Accounts Closed!",accountServiceEventDto.getMessage());
            case UPDATE_ACCOUNT_BALANCE -> sendGridEmailService.sendSingleEmail(toEmail,"Account Balance Updated!",accountServiceEventDto.getMessage());
            case ACCOUNT_FREEZE_UNFREEZE -> sendGridEmailService.sendSingleEmail(toEmail,"Account Freeze Status!",accountServiceEventDto.getMessage());
            default -> log.warn("AccountServiceEventConsumer: Unknown event type!");
        }
    }

    @KafkaListener(
            topics = KafkaTopics.LOAN_SERVICE_EVENT,
            groupId = "loan-kafkaService")
    public void loanServiceEventConsumer(LoanServiceEventDto loanServiceEventDto){

        switch (loanServiceEventDto.getLoanServiceEventType()){
            case LOAN_APPROVAL_STATUS -> sendGridEmailService.sendSingleEmail(toEmail,"Loan Approval Status",loanServiceEventDto.getMessage());
            case LOAN_APPLICATION -> sendGridEmailService.sendSingleEmail(toEmail,"New Loan Application Submitted!",loanServiceEventDto.getMessage());
            default -> log.warn("LoanServiceEventConsumer: Unknown event type!");
        }
    }

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_SERVICE_EVENT,
            groupId = "payment-kafkaService"
    )
    public void paymentServiceEventConsumer(PaymentServiceEventDto paymentServiceEventDto){
        switch (paymentServiceEventDto.getPaymentServiceEventType()){
            case INITIATE_PAYMENT -> {
                String subject = String.format("Payment Status: %s",paymentServiceEventDto.getMetadata().get("paymentStatus"));
                sendGridEmailService.sendSingleEmail(toEmail,subject,paymentServiceEventDto.getMessage());
            }
            default -> log.warn("PaymentServiceEventConsumer: Unknown event type!");
        }
    }

    @KafkaListener(
            topics = KafkaTopics.TRANSACTION_SERVICE_EVENT,
            groupId = "transaction-kafkaService")
    public void transactionEventConsumer(TransactionServiceEventDto<Void> transactionServiceEventDto){
        switch (transactionServiceEventDto.getTransactionServiceEventType()){
            case DEPOSIT -> sendGridEmailService.sendSingleEmail(toEmail,"Account Deposited!",transactionServiceEventDto.getMessage());
            case WITHDRAW -> sendGridEmailService.sendSingleEmail(toEmail,"Account Withdrawal!",transactionServiceEventDto.getMessage());
            default -> log.warn("TransactionEventConsumer: Unknown Event Type!");
        }
    }

    @KafkaListener(
            topics = KafkaTopics.USER_SERVICE_EVENT,
            groupId = "user-kafkaService"
    )
    public void userEventConsumer(UserServiceEventDto userServiceEventDto){
        switch (userServiceEventDto.getUserServiceEventType()){
            case USER_DELETED -> sendGridEmailService.sendSingleEmail(toEmail,"User Account Deleted!",userServiceEventDto.getMessage());
            case UPDATE_PROFILE -> sendGridEmailService.sendSingleEmail(toEmail,"User Account Updated!",userServiceEventDto.getMessage());
            case UPDATE_KYC_STATUS -> sendGridEmailService.sendSingleEmail(toEmail,"User KYC Updated!",userServiceEventDto.getMessage());
            default -> log.warn("UserEventConsumer: Unknown Event Type!");
        }
    }

    ////////////////////////////////////////////////////////////////////////
    //                         TEST                                      //
    //////////////////////////////////////////////////////////////////////

    @KafkaListener(topics = "test-event", groupId = "notification-KafkaService")
    public void testEventConsumer(String message){
        sendGridEmailService.sendSingleEmail("mohasinpatel313@gmail.com","Test-Notification Service", message);

        System.err.println("Test notification received: " + message);
    }
}
