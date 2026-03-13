package com.account_service.kafka;

import com.account_service.repository.AccountRepo;
import com.account_service.service.AccountService;
import com.baas.events.transaction_service.CreditReceiverAccountPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class EventHandlers {
    private final AccountService accountService;
    private final AccountRepo accountRepo;

    @Autowired
    public EventHandlers(AccountService accountService,AccountRepo accountRepo){
        this.accountService=accountService;
        this.accountRepo = accountRepo;
    }

    @Transactional
    public void creditReceiverAccountHandler(CreditReceiverAccountPayload payload){

        log.info("Account: creditReceiverAccountHandler updating the receiver account");

        log.info("EventHandlers: creditReceiverAccountHandler->Fetching the receiver account balance!");
        BigDecimal accountBalance = accountRepo.getAccountBalanceByAccountIdAndUserId(payload.getAccountId(),payload.getUserId());

        log.info("EventHandlers: creditReceiverAccountHandler-> Updating the account balance for the receiver!");
        accountRepo.updateAccountBalance(payload.getAccountId(),payload.getUserId(),accountBalance.add(payload.getAmount()));

        log.info("Account: creditReceiverAccountHandler updated the receiver account");
    }
}
