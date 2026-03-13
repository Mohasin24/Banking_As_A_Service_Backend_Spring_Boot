package com.transaction_service.servicesImpl;

import com.baas.events.transaction_service.CreditReceiverAccountPayload;
import com.transaction_service.client.AccountExistenceResponse;
import com.transaction_service.client.AccountServiceClient;
import com.transaction_service.constant.TransactionStatus;
import com.transaction_service.constant.TransactionType;
import com.transaction_service.dto.TransactionRequest;
import com.transaction_service.dto.TransactionResponse;
import com.transaction_service.dto.TransferRequest;
import com.transaction_service.dto.TransferResponse;
import com.transaction_service.entity.Transaction;
import com.transaction_service.exception.AccountBalanceUpdateFailedException;
import com.transaction_service.exception.InsufficientAccountBalanceException;
import com.transaction_service.exception.InvalidAmountException;
import com.transaction_service.exception.ReceiverAccountNotExistsException;
import com.transaction_service.kafka.TransactionServiceEventProducer;
import com.transaction_service.repository.TransactionRepo;
import com.transaction_service.service.TransactionService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService
{
    private final TransactionRepo transactionRepo;
    private final AccountServiceClient accountServiceClient;
    private final String SELF = "Self";
    private final TransactionServiceEventProducer transactionServiceEventProducer;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepo transactionRepo,
            AccountServiceClient accountServiceClient,
            TransactionServiceEventProducer transactionServiceEventProducer
    ){
        this.transactionRepo = transactionRepo;
        this.accountServiceClient=accountServiceClient;
        this.transactionServiceEventProducer=transactionServiceEventProducer;
    }

    /**********************************************************************************
     *              Deposit
     **********************************************************************************/
    // Update the transaction Logic for internal transactions
    @Override
    @Transactional //for multiple db queries
    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "depositFallback")
    public TransactionResponse deposit(TransactionRequest transactionRequest) {

        if(transactionRequest.amount().signum()==-1){
            log.info("Deposit: Invalid amount entered!");
            throw new InvalidAmountException();
        }

        BigDecimal balance = accountServiceClient.getAccountBalance(transactionRequest.fromAccountId(),transactionRequest.userId());

        // Update to use System account for internal transactions
        int rows = accountServiceClient.updateAccountBalance(transactionRequest.fromAccountId(),transactionRequest.userId(), transactionRequest.amount().add(balance));

        log.info("Deposit: Initiating deposit for userId={}, toAccountId={} for amount={}.",transactionRequest.userId(),
                transactionRequest.toAccountId(),transactionRequest.amount());

        if(rows==0){

            log.info("Deposit: Failed to deposit amount={} into account with userId={} and toAccountId={}",
                    transactionRequest.amount(),transactionRequest.userId(),transactionRequest.toAccountId());

            throw new AccountBalanceUpdateFailedException();
        }

        Transaction transaction = Transaction.builder()
                .toAccountId(transactionRequest.toAccountId())
                .fromAccountId(transactionRequest.fromAccountId())
                .userId(transactionRequest.userId())
                .amount(transactionRequest.amount())
                .transactionType(TransactionType.CREDIT)
                .description(transactionRequest.toAccountId()+"/"+transactionRequest.userId()+SELF+TransactionType.CREDIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        //add check if the transaction is successfully stored in DB or not
        transaction = transactionRepo.save(transaction);

        //Kafka Event
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("transactionId",transaction.getTransactionId());
        metadata.put("userId",transaction.getUserId());
        metadata.put("amount",transaction.getAmount().longValue());
        metadata.put("transactionType",transaction.getTransactionType().toString());
        metadata.put("description",transaction.getDescription());
        metadata.put("transactionDate",transaction.getTransactionDate().toString());
        metadata.put("failureReason",transaction.getFailureReason());
        metadata.put("transactionStatus",transaction.getTransactionStatus().toString());

        transactionServiceEventProducer.depositEvent(String.valueOf(transaction.getUserId()),metadata);

        return mapToResponse(transaction);
    }

    /**********************************************************************************
     *                    CircuitBreaker DepositFallback
     *********************************************************************************/
    public TransactionResponse depositFallback(TransactionRequest transactionRequest, Throwable ex){

        log.error("TransactionServiceImpl - DepositFallback triggered: {}",ex.getMessage());

        Transaction transaction = Transaction.builder()
                .toAccountId(transactionRequest.toAccountId())
                .fromAccountId(transactionRequest.fromAccountId())
                .userId(transactionRequest.userId())
                .amount(transactionRequest.amount())
                .transactionType(TransactionType.CREDIT)
                .description(transactionRequest.toAccountId()+"/"+transactionRequest.userId()+"/"+SELF+TransactionType.CREDIT)
                .transactionStatus(TransactionStatus.FAILED)
                .failureReason("Account Service not available!")
                .build();

        //add check if the transaction is successfully stored in DB or not
        transaction = transactionRepo.save(transaction);

        return mapToResponse(transaction);
    }

    /**********************************************************************************
     *              Withdraw
     **********************************************************************************/
    // Update the transaction Logic for internal transactions
    @Override
    @Transactional //for multiple db queries
    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "withdrawFallback")
    public TransactionResponse withdraw(TransactionRequest transactionRequest) {

        BigDecimal balance = accountServiceClient.getAccountBalance(transactionRequest.toAccountId(),transactionRequest.userId());

        log.info("Withdraw: Fetching account balance for userId={} and accountId={}",
                transactionRequest.userId(),transactionRequest.toAccountId());

        if(balance.compareTo(transactionRequest.amount()) < 0){

            log.info("Withdraw: Terminating the transaction due to insufficient account balance, userId={}, " +
                    "accountId={}", transactionRequest.userId(),transactionRequest.toAccountId());

            throw new InsufficientAccountBalanceException();
        }

        log.info("Withdraw: Updating account balance for userId={} and accountId={}",
                transactionRequest.userId(),transactionRequest.toAccountId());

        int rows = accountServiceClient.updateAccountBalance(transactionRequest.toAccountId(),
                transactionRequest.userId(), balance.subtract(transactionRequest.amount()));

        if(rows==0){

            log.info("Withdraw: Failed to update account balance for userId={} and accountId={}",
                    transactionRequest.userId(),transactionRequest.toAccountId());

            throw new AccountBalanceUpdateFailedException();
        }

        Transaction transaction = Transaction.builder()
                .userId(transactionRequest.userId())
                .toAccountId(transactionRequest.toAccountId())
                .amount(transactionRequest.amount())
                .transactionType(TransactionType.DEBIT)
                .description(transactionRequest.toAccountId()+"/"+transactionRequest.userId()+"/"+SELF+"/"+TransactionType.DEBIT)
                .failureReason(null)
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transaction = transactionRepo.save(transaction);

        //Kafka Event
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("transactionId",transaction.getTransactionId());
        metadata.put("userId",transaction.getUserId());
        metadata.put("amount",transaction.getAmount().longValue());
        metadata.put("transactionType",transaction.getTransactionType().toString());
        metadata.put("description",transaction.getDescription());
        metadata.put("transactionDate",transaction.getTransactionDate().toString());
        metadata.put("failureReason",transaction.getFailureReason());
        metadata.put("transactionStatus",transaction.getTransactionStatus().toString());

        transactionServiceEventProducer.depositEvent(String.valueOf(transaction.getUserId()),metadata);

        return mapToResponse(transaction);
    }

    /**********************************************************************************
     *                  CircuitBreaker WithdrawFallback
     **********************************************************************************/
    public TransactionResponse withdrawFallback(TransactionRequest transactionRequest, Throwable ex){

        log.error("TransactionServiceImpl - WithdrawFallback : {}", ex.getMessage());

        Transaction transaction = Transaction.builder()
                .userId(transactionRequest.userId())
                .toAccountId(transactionRequest.toAccountId())
                .amount(transactionRequest.amount())
                .transactionType(TransactionType.DEBIT)
                .description(transactionRequest.toAccountId()+"/"+transactionRequest.userId()+"/"+SELF+"/"+TransactionType.DEBIT)
                .failureReason("Account Service not available!")
                .transactionStatus(TransactionStatus.FAILED)
                .build();

        transaction = transactionRepo.save(transaction);

        return mapToResponse(transaction);
    }

    /**********************************************************************************
     *                  Transfer
     **********************************************************************************/
    @Override
    @Transactional //for multiple db queries
    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "transferFallback")
    public TransferResponse transfer(TransferRequest transferRequest) {

        //Validate if the receiver account exists or not
        log.info("Transfer: Validating the receivers account details with toAccountId={}",transferRequest.toAccountId());
        AccountExistenceResponse receiverAccount =
                accountServiceClient.checkIfUserAccountExists(transferRequest.toAccountId());

        if(receiverAccount==null){
            throw new ReceiverAccountNotExistsException(String.format("Receiver Account with account id: %d not " +
                    "exists!",transferRequest.toAccountId()));
        }

        log.info("Transfer: Validated the receivers account details with toAccountId={}",transferRequest.toAccountId());

        log.info("Transfer: Fetching account balance for userId={} and accountId={}",
                transferRequest.userId(),transferRequest.fromAccountId());

        // Validating the account balance
        BigDecimal balance = accountServiceClient.getAccountBalance(transferRequest.fromAccountId(),transferRequest.userId());
        
        log.info("Account Balance: {}",balance);

        if(balance.compareTo(transferRequest.amount()) < 0){

            log.info("Transfer: Terminating the transaction due to insufficient account balance, userId={}, " +
                    "accountId={}", transferRequest.userId(),transferRequest.fromAccountId());

            throw new InsufficientAccountBalanceException();
        }

        log.info("Transfer: Updating the sender account balance!");

        int sender = accountServiceClient.updateAccountBalance(transferRequest.fromAccountId(),
                transferRequest.userId(), balance.subtract(transferRequest.amount()));

        if(sender==0){

            log.info("Transfer: Failed to update account balance for userId={} and accountId={}", transferRequest.userId(),transferRequest.fromAccountId());

            throw new AccountBalanceUpdateFailedException();
        }

        log.info("Transfer: Updated the sender account balance!");

        log.info("Transfer: saving the transaction details");

        // save transaction details
        Transaction transaction = Transaction.builder()
                .userId(transferRequest.userId())
                .fromAccountId(transferRequest.fromAccountId())
                .toAccountId(transferRequest.toAccountId())
                .amount(transferRequest.amount())
                .transactionType(TransactionType.DEBIT)
                .description(
                        transferRequest.fromAccountId()+"/"+transferRequest.userId()+"/"+TransactionType.DEBIT.toString()+"/"+transferRequest.toAccountId()
                )
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transaction = transactionRepo.save(transaction);

        log.info("Transfer: Transaction saved!");

        log.info("Transfer: Updating the receiver account!");

        // Update the balance update logic for receiver bank account with the kafka event
        // accountId, receiverUserId, amount
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId",transaction.getUserId());
        metadata.put("fromAccountId",transaction.getFromAccountId());
        metadata.put("toAccountId",transaction.getToAccountId());
        metadata.put("amount",transaction.getAmount());

        log.info("TransactionServiceImpl: printing the receiver account details: {}",receiverAccount.toString());

        CreditReceiverAccountPayload creditReceiverAccountPayload = CreditReceiverAccountPayload.builder()
                .userId(receiverAccount.userId())
                .accountId(receiverAccount.accountId())
                .accountNo(receiverAccount.accountNo())
                .amount(transaction.getAmount())
                .accountType(receiverAccount.accountType())
                .accountStatus(receiverAccount.accountStatus())
                .build();

        log.info("TransactionServiceImpl: moving to transactionServiceEventProducer.updateReceiverAccountEvent");

        transactionServiceEventProducer.updateReceiverAccountEvent(
                String.valueOf(transferRequest.userId()),
                transaction.getTransactionId(),
                metadata,
                creditReceiverAccountPayload
        );

        log.info("Transfer: receiver account updated successfully!");

        return TransferResponse.builder()
                .transactionId(transaction.getTransactionId())
                .transactionStatus(transaction.getTransactionStatus())
                .failureReason(transaction.getFailureReason())
                .build();
    }

    /**********************************************************************************
     *                  CircuitBreaker TransferFallback
     **********************************************************************************/
    public TransactionResponse transferFallback(TransferRequest transferRequest, Throwable ex){

        log.error("TransactionServiceImpl - TransferFallback triggered : {}", ex.getMessage());

        Transaction transaction = Transaction.builder()
                .userId(transferRequest.userId())
                .fromAccountId(transferRequest.fromAccountId())
                .toAccountId(transferRequest.toAccountId())
                .amount(transferRequest.amount())
                .transactionType(TransactionType.DEBIT)
                .description(
                        transferRequest.fromAccountId()+"/"+transferRequest.userId()+"/"+TransactionType.DEBIT+"/"+transferRequest.toAccountId()
                )
                .transactionStatus(TransactionStatus.FAILED)
                .failureReason("Account Service not available!")
                .build();

        transaction = transactionRepo.save(transaction);

        return mapToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getTransactionsByFromAccountId(Long fromAccountId) {
        List<Transaction> transactionList = transactionRepo.findByFromAccountId(fromAccountId);

        return transactionList.stream().map(transaction->mapToResponse(transaction)
        ).collect(Collectors.toList());

    }

    @Override
    public List<TransactionResponse> getTransactionsForAllAccounts(Long userId) {

        List<Transaction> transactionList = transactionRepo.findByUserId(userId);

        return transactionList.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    ////////////////////////////////////////////////////////////////////////
    //                              Utility                               //
    ////////////////////////////////////////////////////////////////////////

    private TransactionResponse mapToResponse(Transaction transaction){
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .userId(transaction.getUserId())
                .toAccountId(transaction.getToAccountId())
                .fromAccountId(transaction.getFromAccountId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType().toString())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .transactionStatus(transaction.getTransactionStatus())
                .failureReason(transaction.getFailureReason())
                .build();
    }
}