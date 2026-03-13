package com.payment_service.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

//@Service
@Slf4j
public class AccountServiceClient {

//    @Value("${services.account-service-uri}")
    private String accountServiceBaseUri;

    private final WebClient webClient;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public AccountServiceClient(WebClient webClient,HttpServletRequest httpServletRequest){
        this.webClient = webClient;
        this.httpServletRequest=httpServletRequest;
    }

    public BigDecimal getAccountBalance(long accountId, long userId){
        String uri = accountServiceBaseUri + "/balance/"+userId+"/"+accountId;
        log.info(uri);

        return webClient
                .get()
                .uri(uri)
                .header(extractToken())
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();
    }

    public int updateAccountBalance(long accountId, long userId, BigDecimal amount){
        String uri = accountServiceBaseUri + "/update-balance/" +userId+"/"+accountId;
        log.info(uri);

        return webClient
                .patch()
                .uri(uri)
                .header(extractToken())
                .bodyValue(amount)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    private String extractToken(){
        return httpServletRequest.getHeader("Authorization");
    }
}
