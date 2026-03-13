package com.transaction_service.service;

import com.transaction_service.dto.TransactionRequest;
import com.transaction_service.dto.TransactionResponse;
import com.transaction_service.dto.TransferRequest;
import com.transaction_service.dto.TransferResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {

    TransactionResponse deposit(TransactionRequest request);

    TransactionResponse withdraw( TransactionRequest request);

    TransferResponse transfer(TransferRequest request);

    List<TransactionResponse> getTransactionsByFromAccountId(Long accountId);

    List<TransactionResponse> getTransactionsForAllAccounts(Long userId);
}
