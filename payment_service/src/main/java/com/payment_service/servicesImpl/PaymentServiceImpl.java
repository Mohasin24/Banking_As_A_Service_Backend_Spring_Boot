package com.payment_service.servicesImpl;

import com.payment_service.client.TransactionServiceClient;
import com.payment_service.constant.PaymentStatus;
import com.payment_service.dto.*;
import com.payment_service.entity.Payment;
import com.payment_service.kafka.PaymentServiceEventProducer;
import com.payment_service.repository.PaymentRepo;
import com.payment_service.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final TransactionServiceClient transactionServiceClient;
    private final PaymentServiceEventProducer paymentServiceEventProducer;

    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepo,TransactionServiceClient transactionServiceClient,PaymentServiceEventProducer paymentServiceEventProducer){
        this.paymentRepo=paymentRepo;
        this.transactionServiceClient=transactionServiceClient;
        this.paymentServiceEventProducer=paymentServiceEventProducer;
    }

    /**********************************************************************************
     *                      InitiatePayment
     **********************************************************************************/
    @Override
    @CircuitBreaker(name = "transactionServiceCB", fallbackMethod = "initiatePaymentFallback")
    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {
        //create a payment entry with pending state in the payment db
        Payment payment = Payment.builder()
                .userId(paymentRequest.userId())
                .refTransactionId(null)
                .fromAccountId(paymentRequest.fromAccountId())
                .toAccountId(paymentRequest.toAccountId())
                .paymentType(paymentRequest.paymentType())
                .amount(paymentRequest.amount())
                .paymentStatus(PaymentStatus.PENDING)
                .failureReason(null)
                .build();

        Payment savedPayment = paymentRepo.save(payment);

        // call Transaction service
        TransferResponse transferResponse =
                transactionServiceClient.transfer(mapToTransactionRequest(paymentRequest));

        // verify transaction service response use boolean in Transaction Service response
        if(transferResponse.transactionId().isBlank() && transferResponse.transactionStatus().toString().equals(PaymentStatus.FAILED.toString())){
            log.error("Payment failed: Id - {}, Reason - {}",savedPayment.getPaymentId(),transferResponse.failureReason());

            savedPayment.setPaymentStatus(PaymentStatus.FAILED);
            savedPayment.setFailureReason(transferResponse.failureReason());

            savedPayment = paymentRepo.save(savedPayment);

            String message = "Payment Failed : " + transferResponse.failureReason();

            //Publish Kafka Event for Failed Payment
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("userId",savedPayment.getUserId());
            metadata.put("paymentId",savedPayment.getPaymentId());
            metadata.put("paymentType",payment.getPaymentType().toString());
            metadata.put("paymentStatus",payment.getPaymentStatus().toString());
            metadata.put("failureReason",savedPayment.getFailureReason());

            paymentServiceEventProducer.initiatePaymentEvent(String.valueOf(savedPayment.getUserId()),metadata);

            throw new RuntimeException(message);

        } else if (!transferResponse.transactionId().isBlank() && transferResponse.transactionStatus().toString().equals(PaymentStatus.SUCCESS.toString())) {
            log.info("Payment Success: Id - {}", savedPayment.getPaymentId());

            savedPayment.setRefTransactionId(transferResponse.transactionId());
            savedPayment.setPaymentStatus(PaymentStatus.SUCCESS);

            savedPayment = paymentRepo.save(savedPayment);

            //Publish Kafka Event for Succeeded Payment
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("userId",savedPayment.getUserId());
            metadata.put("paymentId",savedPayment.getPaymentId());
            metadata.put("paymentType",payment.getPaymentType().toString());
            metadata.put("paymentStatus",payment.getPaymentStatus().toString());
            metadata.put("failureReason",savedPayment.getFailureReason());

            paymentServiceEventProducer.initiatePaymentEvent(String.valueOf(savedPayment.getUserId()),metadata);

        }

        // return response
        return mapToPaymentResponse(savedPayment);
    }

    /**********************************************************************************
     *              CircuitBreaker InitiatePaymentFallback
     **********************************************************************************/
    public PaymentResponse initiatePaymentFallback(PaymentRequest paymentRequest, Throwable ex){

        log.error("PaymentServiceImpl - initiatePaymentFallback triggered : {}", ex.getMessage());

        Payment payment = Payment.builder()
                .userId(paymentRequest.userId())
                .refTransactionId(null)
                .fromAccountId(paymentRequest.fromAccountId())
                .toAccountId(paymentRequest.toAccountId())
                .paymentType(paymentRequest.paymentType())
                .amount(paymentRequest.amount())
                .paymentStatus(PaymentStatus.FAILED)
                .failureReason("Transaction Service is not available!")
                .build();

        Payment savedPayment = paymentRepo.save(payment);

        return mapToPaymentResponse(savedPayment);
    }

    /**********************************************************************************
     *                  UserAllPaymentDetails
     **********************************************************************************/
    //update
    @Override
    public List<PaymentResponse> userAllPaymentDetails(long userId) {
        // return user all payment details list by userId

        // publish event with the PDF

        return paymentRepo.findAll().stream().map(this::mapToPaymentResponse).collect(Collectors.toList());
    }

    /**********************************************************************************
     *              UserPaymentDetails
     **********************************************************************************/
    @Override
    public PaymentResponse userPaymentDetails(long paymentId, long userId) {
        // return specific payment details for a user by userId
        return mapToPaymentResponse(paymentRepo.findByPaymentIdAndUserId(paymentId,userId));
    }

    /**********************************************************************************
     *                    UserPaymentStatus
     **********************************************************************************/
    @Override
    public String userPaymentStatus(long paymentId) {
        return paymentRepo.findPaymentStatusByPaymentId(paymentId);
    }

    ////////////////////////////////////////////////////////////////////////
    //                         UTIL                                       //
    ////////////////////////////////////////////////////////////////////////


    private TransactionRequest mapToTransactionRequest(PaymentRequest paymentRequest){
        return TransactionRequest.builder()
                .userId(paymentRequest.userId())
                .fromAccountId(paymentRequest.fromAccountId())
                .toAccountId(paymentRequest.toAccountId())
                .amount(paymentRequest.amount())
                .build();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment){
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .transactionId(payment.getRefTransactionId())
                .userId(payment.getUserId())
                .fromAccountId(payment.getFromAccountId())
                .toAccountId(payment.getToAccountId())
                .paymentType(payment.getPaymentType())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .paymentFailureReason(payment.getFailureReason())
                .build();
    }
}
