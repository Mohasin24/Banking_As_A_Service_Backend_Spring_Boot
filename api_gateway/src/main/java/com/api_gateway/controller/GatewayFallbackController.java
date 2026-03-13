package com.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {

    @GetMapping("/account")
    public String accountServiceFallback(){
        return "Account service is currently unavailable";
    }

    @GetMapping("/authentication")
    public String authenticationServiceFallback(){
        return "Authentication service is currently unavailable";
    }

    @GetMapping("/loan")
    public String loanServiceFallback(){
        return "Loan service is currently unavailable";
    }

    @GetMapping("/notification")
    public String notificationServiceFallback(){
        return "Notification service is currently unavailable";
    }

    @GetMapping("/payment")
    public String paymentServiceFallback(){
        return "Payment service is currently unavailable";
    }

    @GetMapping("/transaction")
    public String transactionServiceFallback(){
        return "Transaction service is currently unavailable";
    }

    @GetMapping("/user")
    public String userServiceFallback(){
        return "User service is currently unavailable";
    }
}
