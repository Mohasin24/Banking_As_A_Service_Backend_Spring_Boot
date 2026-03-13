package com.transaction_service.client;

import com.transaction_service.dto.BalanceUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
@Slf4j
public class AccountServiceClient {

    private final WebClient webClient;
    private final HttpServletRequest httpServletRequest;

    @Value("${services.account-service-uri}")
    private String accountServiceBaseUri;
//    private String accountServiceBaseUri="http://ACCOUNT-SERVICE/api/v1/account";

    @Autowired
    public AccountServiceClient(WebClient webClient,HttpServletRequest httpServletRequest){
        this.webClient = webClient;
        this.httpServletRequest=httpServletRequest;
    }

    public BigDecimal getAccountBalance(long accountId, long userId){
        String uri = accountServiceBaseUri + "/balance/"+userId+"/"+accountId;
        String token = extractToken();
        log.info(uri);
        System.err.println(uri);
        return webClient
                .get()
                .uri(uri)
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();
    }

    public int updateAccountBalance(long accountId, long userId, BigDecimal amount){
        String uri = accountServiceBaseUri + "/update-balance/" +userId+"/"+accountId;
        String token = extractToken();
        log.warn(uri);
        log.warn("Inside Account Client");
        return webClient
                .patch()
                .uri(uri)
                .header("Authorization",token)
                .bodyValue(new BalanceUpdateRequest(amount))
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    public AccountExistenceResponse checkIfUserAccountExists(long accountId){
        String uri = accountServiceBaseUri + "/account-exists/" + accountId;
        String token = extractToken();

        return webClient
                .get()
                .uri(uri)
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(AccountExistenceResponse.class)
                .block();
    }

    private String extractToken(){
        return httpServletRequest.getHeader("Authorization");
    }
}
