package com.transaction_service.controller;

import com.transaction_service.dto.TransferRequest;
import com.transaction_service.dto.TransferResponse;
import com.transaction_service.kafka.TransactionServiceEventProducer;
import com.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction/internal")
public class TransactionControllerInternal {

    private final TransactionService transactionService;
    private final TransactionServiceEventProducer transactionServiceEventProducer;

    @Autowired
    public TransactionControllerInternal(TransactionService transactionService, TransactionServiceEventProducer transactionServiceEventProducer){
        this.transactionService=transactionService;
        this.transactionServiceEventProducer = transactionServiceEventProducer;
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Post Mapping                               //
    ////////////////////////////////////////////////////////////////////////


    @PostMapping("/fund-transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest){
        return ResponseEntity.ok(transactionService.transfer(transferRequest));
    }

}
