package com.account_service.config;

import com.account_service.constant.AccountStatus;
import com.account_service.constant.AccountType;
import com.account_service.constant.SystemAccountType;
import com.account_service.entity.Account;
import com.account_service.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SystemAccountInitializer implements ApplicationRunner {

    private final AccountRepo accountRepo;

    @Autowired
    public SystemAccountInitializer(AccountRepo accountRepo){
        this.accountRepo=accountRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for(SystemAccountType type : SystemAccountType.values()){
            accountRepo
                    .findByAccountType(AccountType.SYSTEM)
                    .orElseGet(()->createSystemAccount(type));
        }
    }

    private Account createSystemAccount(SystemAccountType type){
        Account account = Account.builder()
                .userId(null)
                .accountNo(type.toString())
                .accountType(AccountType.SYSTEM)
                .systemAccountType(type)
                .accountStatus(AccountStatus.ACTIVE)
                .accountBalance(BigDecimal.valueOf(0.0))
                .build();

        return accountRepo.save(account);
    }
}
