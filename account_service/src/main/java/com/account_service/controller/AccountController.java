package com.account_service.controller;

import com.account_service.dto.AccountExistenceResponse;
import com.account_service.dto.AccountRequest;
import com.account_service.dto.BalanceUpdateRequest;
import com.account_service.entity.Account;
import com.account_service.exception.AccountDeletionFailedException;
import com.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService=accountService;
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Get Mapping                                //
    ////////////////////////////////////////////////////////////////////////

    @GetMapping("/{user-id}/{account-id}")
    public ResponseEntity<?> getAccountDetails(@PathVariable("user-id") long userId,
                                               @PathVariable("account-id")long accountId){

        log.info("AccountController: GetAccountDetails -> userId: {} and accountId: {}",userId,accountId);

        return ResponseEntity.ok(accountService.getAccount(accountId,userId));
    }

    @GetMapping("/all/{user-id}")
    public ResponseEntity<?> getAllAccountsByUserId(@PathVariable("user-id") long userId){
        return ResponseEntity.ok(accountService.getAllAccountsByUserId(userId));
    }

    @GetMapping("/balance/{user-id}/{account-id}")
    public ResponseEntity<Long> getAccountBalance(@PathVariable("user-id") long userId,
                                               @PathVariable("account-id")long accountId){
        return ResponseEntity.ok(accountService.getAccountBalance(accountId,userId).longValue());
    }

    @GetMapping("/owner/{user-id}/{account-id}")
    public ResponseEntity<?> isAccountOwnedByUser(@PathVariable("user-id") long userId,
                                                  @PathVariable("account-id")long accountId){
        return ResponseEntity.ok(accountService.isAccountOwnedByUser(accountId,userId));
    }

    @GetMapping("status/{user-id}/{account-id}")
    public ResponseEntity<?> getAccountStatus(@PathVariable("user-id") long userId,
                                              @PathVariable("account-id")long accountId){
        return ResponseEntity.ok(accountService.getAccountStatus(accountId,userId));
    }

    @GetMapping("/account-exists/{accountId}")
    public ResponseEntity<AccountExistenceResponse> checkAccountExistence(@PathVariable("accountId") long accountId){

        Account account = accountService.accountExists(accountId);

        AccountExistenceResponse accountExistenceResponse = AccountExistenceResponse.builder()
                .userId(account.getUserId())
                .accountId(account.getAccountId())
                .accountNo(account.getAccountNo())
                .accountStatus(account.getAccountStatus().toString())
                .accountType(account.getAccountType().toString())
                .build();

        return ResponseEntity.ok(accountExistenceResponse);
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Post Mapping                               //
    ////////////////////////////////////////////////////////////////////////

    @PostMapping("/open")
    public ResponseEntity<?> openAccount(@RequestBody AccountRequest accountRequest){
        return ResponseEntity.ok(accountService.openAccount(accountRequest));
    }

    @PostMapping("/close-all/{user-id}")
    public ResponseEntity<?> closeAllAccountsByUserId(@PathVariable("user-id") long userId){
        return ResponseEntity.ok(accountService.closeAllAccountsByUserId(userId));
    }


    ////////////////////////////////////////////////////////////////////////
    //                         Patch Mapping                              //
    ////////////////////////////////////////////////////////////////////////

    @PatchMapping("/freeze/{user-id}/{account-id}")
    public ResponseEntity<?> freezeAccount(@PathVariable("user-id") long userId,
                                           @PathVariable("account-id")long accountId){
        return ResponseEntity.ok(accountService.freezeAccount(accountId,userId));
    }

    @PatchMapping("/unfreeze/{user-id}/{account-id}")
    public ResponseEntity<?> unFreezeAccount(@PathVariable("user-id") long userId,
                                             @PathVariable("account-id")long accountId){
        return ResponseEntity.ok(accountService.unfreezeAccount(accountId,userId));
    }

    // review the code
    @PatchMapping("/update-balance/{user-id}/{account-id}")
    public ResponseEntity<?> updateAccountBalance(
            @PathVariable("user-id") long userId,
            @PathVariable("account-id") long accountId,
            @RequestBody BalanceUpdateRequest balanceUpdateRequest
            ){
        log.warn("Account: Inside the update balance");
        return ResponseEntity.ok(accountService.updateAccountBalance(accountId,userId,balanceUpdateRequest.amount()));
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Delete Mapping                             //
    ////////////////////////////////////////////////////////////////////////

    @DeleteMapping("/close/{user-id}/{account-id}")
    public ResponseEntity<?> closeAccount(@PathVariable("user-id") long userId,
                                          @PathVariable("account-id")long accountId){
        if(accountService.closeAccount(accountId,userId)){
            return ResponseEntity.ok("Account with account id: "+accountId+" deleted successfully!");
        }

        throw new AccountDeletionFailedException("Failed to delete account with account id: {accountId}"+accountId);
    }

    ////////////////////////////////////////////////////////////////////////
    //                           Test Mapping                             //
    ////////////////////////////////////////////////////////////////////////
    @GetMapping("/test")
    public String test(){
        log.info("Account: Controller test endpoint");
        return "Account Service Test";
    }

}
