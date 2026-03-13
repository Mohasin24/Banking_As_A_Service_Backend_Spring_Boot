package com.account_service.controller;

import com.account_service.constant.AccountType;
import com.account_service.entity.Account;
import com.account_service.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account/internal")
public class AccountControllerInternal {

    private final AccountRepo accountRepo;

    @Autowired
    public AccountControllerInternal(AccountRepo accountRepo){
        this.accountRepo=accountRepo;
    }

    @GetMapping("/system-acc")
    public ResponseEntity<List<Account>> getSystemAccountDetails(){
        return ResponseEntity.ok(accountRepo.findAllByAccountType(AccountType.SYSTEM));
    }
}
