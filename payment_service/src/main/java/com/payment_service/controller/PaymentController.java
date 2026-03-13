package com.payment_service.controller;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController
{
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(paymentService.initiatePayment(paymentRequest));
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<String> getPaymentStatus(@PathVariable("paymentId") long paymentId ){
        return ResponseEntity.ok(paymentService.userPaymentStatus(paymentId));
    }

    @GetMapping("/{userId}/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(
            @PathVariable("userId") long userId,
            @PathVariable("paymentId") long paymentId
    ){

        return ResponseEntity.ok(paymentService.userPaymentDetails(userId,paymentId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentResponse>> getAllPaymentDetails(@PathVariable("userId") long userId){

        return ResponseEntity.ok(paymentService.userAllPaymentDetails(userId));
    }

    @PostMapping("/retry/{userId}/{paymentId}")
    public ResponseEntity<?> retryPayment(
            @PathVariable("userId") long userId,
            @PathVariable("paymentId") long paymentId
    ){

        //Update the logic here
        return null;
    }

    ////////////////////////////////////////////////////////////////////////
    //                                TEST                                //
    ////////////////////////////////////////////////////////////////////////

    @GetMapping("/test")
    public String test(){
        return "PaymentController controller";
    }
}