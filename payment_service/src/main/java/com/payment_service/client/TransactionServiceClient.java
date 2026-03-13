package com.payment_service.client;

import com.payment_service.dto.*;
import com.payment_service.repository.PaymentRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class TransactionServiceClient {

    @Value("${services.transaction-service-uri}")
    private String transactionServiceBaseUri;

    private final WebClient webClient;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public TransactionServiceClient(WebClient webClient,HttpServletRequest httpServletRequest){
        this.webClient = webClient;
        this.httpServletRequest=httpServletRequest;
    }

    public TransferResponse transfer(TransactionRequest transactionRequest){
        String uri = transactionServiceBaseUri + "/internal/fund-transfer";
        log.info(uri);
        
        return webClient
                .post()
                .uri(uri)
                .header("Authorization",extractToken())
                .bodyValue(transactionRequest)
                .retrieve()
                .bodyToMono(TransferResponse.class)
                .block();

    }

    private String extractToken(){
        return httpServletRequest.getHeader("Authorization");
    }
}
