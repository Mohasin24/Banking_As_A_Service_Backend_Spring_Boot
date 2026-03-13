package com.payment_service.service;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {

    PaymentResponse initiatePayment(PaymentRequest paymentRequest);

    List<PaymentResponse> userAllPaymentDetails(long userId);

    PaymentResponse userPaymentDetails(long paymentId, long userId);

    String userPaymentStatus(long paymentId);
}
