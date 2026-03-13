package com.transaction_service.controller;

import com.transaction_service.dto.TransactionRequest;
import com.transaction_service.dto.TransactionResponse;
import com.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController
{
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService=transactionService;
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Get Mapping                                //
    ////////////////////////////////////////////////////////////////////////

    @GetMapping("/account-id/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountId(@PathVariable("accountId") Long accountId){
        return ResponseEntity.ok(transactionService.getTransactionsByFromAccountId(accountId));
    }

    @GetMapping("/all-accounts/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsForAllAccounts(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(transactionService.getTransactionsForAllAccounts(userId));
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Post Mapping                               //
    ////////////////////////////////////////////////////////////////////////

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest transactionRequest){
        System.err.println("Deposit event deployed!");
        return ResponseEntity.ok(transactionService.deposit(transactionRequest));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.withdraw(transactionRequest));
    }


    ////////////////////////////////////////////////////////////////////////
    //                         Patch Mapping                              //
    ////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////
    //                         Delete Mapping                             //
    ////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////
    //                                Test                                //
    ////////////////////////////////////////////////////////////////////////
    @GetMapping("/test")
    public String test(){
        System.err.println("Test Endpoint");
        return "TransactionController controller";
    }
}