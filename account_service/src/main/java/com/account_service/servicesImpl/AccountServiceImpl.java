package com.account_service.servicesImpl;

import com.account_service.constant.AccountStatus;
import com.account_service.constant.AccountType;
import com.account_service.dto.AccountRequest;
import com.account_service.dto.AccountResponse;
import com.account_service.dto.AccountStatusResponse;
import com.account_service.entity.Account;
import com.account_service.kafka.AccountServiceEventProducer;
import com.account_service.repository.AccountRepo;
import com.account_service.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.account_service.util.Util.generateAccountNo;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final AccountServiceEventProducer eventProducer;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo, AccountServiceEventProducer eventProducer){
        this.accountRepo=accountRepo;
        this.eventProducer=eventProducer;

    }

    @Override
    public AccountResponse openAccount(AccountRequest accountRequest) {

        final String ACCOUNT_NO = generateAccountNo(accountRequest);

       Account account = Account.builder()
               .userId(accountRequest.userId())
               .accountNo(ACCOUNT_NO)
               .accountType(AccountType.valueOf(accountRequest.accountType().toUpperCase()))
               .accountStatus(AccountStatus.ACTIVE)
               .accountBalance(BigDecimal.valueOf(0.0))
               .build();

       //###########check why constructors not working with lombok
//        Account account = new Account(
//                accountRequest.userId()
//        );

        Account savedAccount=accountRepo.save(account);

        //kafka Notification
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("accountId",savedAccount.getAccountId());
        eventProducer.openAccountEvent(
                String.valueOf(savedAccount.getUserId())
                ,metadata
        );

        return AccountResponse.builder()
                .accountId(savedAccount.getAccountId())
                .userId(savedAccount.getUserId())
                .accountNo(savedAccount.getAccountNo())
                .accountType(savedAccount.getAccountType())
                .accountStatus(savedAccount.getAccountStatus())
                .accountBalance(savedAccount.getAccountBalance())
                .createdAt(savedAccount.getCreatedAt())
                .updatedAt(savedAccount.getUpdatedAt())
                .build();
    }

    @Override
    public boolean closeAccount(long accountId, long userId) {

        Account account = accountRepo.findByAccountIdAndUserId(accountId,userId).get();

        accountRepo.delete(account);

        if(accountRepo.existsById(userId)){
            throw new RuntimeException("Failed to delete the Account");
        }

        //kafka Notification
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("accountId",accountId);

        eventProducer.closeAccountEvent(
                String.valueOf(userId)
                ,metadata
        );

        return true;
    }

    @Override
    public AccountResponse getAccount(long accountId, long userId) {
        Account account = accountRepo.findByAccountIdAndUserId(accountId,userId).get();

        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .userId(account.getUserId())
                .accountNo(account.getAccountNo())
                .accountType(account.getAccountType())
                .accountStatus(account.getAccountStatus())
                .accountBalance(account.getAccountBalance())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    @Override
    public List<AccountResponse> getAllAccountsByUserId(long userId) {

        List<Account> accountList = accountRepo.findAllAccountsByUserId(userId);

        List<AccountResponse> accountResponsesList = accountList.stream().map(account -> {
            AccountResponse accountResponse = AccountResponse.builder()
                    .accountId(account.getAccountId())
                    .userId(account.getUserId())
                    .accountNo(account.getAccountNo())
                    .accountType(account.getAccountType())
                    .accountStatus(account.getAccountStatus())
                    .accountBalance(account.getAccountBalance())
                    .createdAt(account.getCreatedAt())
                    .updatedAt(account.getUpdatedAt())
                    .build();

            return accountResponse;
        }).collect(Collectors.toList());


        return accountResponsesList;
    }


    @Override
    public boolean freezeAccount(long accountId, long userId) {
        Account account = accountRepo.findByAccountIdAndUserId(accountId,userId).orElseThrow(
                ()->new NoSuchElementException(String.format("Account with userId: %d and accountId: %d not " +
                        "available!", userId,accountId))
        );

        account.setAccountStatus(AccountStatus.FREEZE);
        Account updatedAccount = accountRepo.save(account);

        if(!updatedAccount.getAccountStatus().name().equals(AccountStatus.FREEZE.name())){
            throw new RuntimeException(String.format("Failed to Freeze account with userId: %d and accountId: %d ! ",
                    userId,account));
        }

        //Kafka Notification
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("accountFreezeStatus", updatedAccount.getAccountStatus());

        eventProducer.accountFreezeStatusEvent(
                String.valueOf(updatedAccount.getUserId()),
                metadata
        );

        return true;
    }

    @Override
    public boolean unfreezeAccount(long accountId, long userId) {

        Account account = accountRepo.findByAccountIdAndUserId(accountId,userId).orElseThrow(
                ()->new NoSuchElementException(String.format("Account with userId: %d and accountId: %d not " +
                        "available!", userId,accountId))
        );
        account.setAccountStatus(AccountStatus.ACTIVE);
        Account updatedAccount = accountRepo.save(account);

        if(!updatedAccount.getAccountStatus().name().equals(AccountStatus.ACTIVE.name())){
            throw new RuntimeException(String.format("Failed to Un-Freeze account with userId: %d and accountId: %d !", userId,account));
        }

        // Kafka Notification

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("accountFreezeStatus",updatedAccount.getAccountStatus());

        eventProducer.accountFreezeStatusEvent(
                String.valueOf(updatedAccount.getUserId()),
                metadata
        );

        return true;

    }

    @Override
    public BigDecimal getAccountBalance(long accountId, long userId) {
        return accountRepo.getAccountBalanceByAccountIdAndUserId(accountId,userId);
    }

    @Override
    public int updateAccountBalance(long accountId, long userId, BigDecimal amount) {

        //Before updating the account balance check the account status
        log.info("Account: In updateAccountBalance amount:{}",amount);
        int result = accountRepo.updateAccountBalance(accountId,userId,amount);

        if(result>1){
            throw new RuntimeException("Failed to update the account balance, more multiple account exists!");
        } else if (result==0) {
            throw new RuntimeException("Failed to update the account balance, account not exists!");
        }

        //kafka Notification
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("accountId",accountId);
        metadata.put("amount", amount);

        eventProducer.updateAccountBalanceEvent(
                String.valueOf(userId)
                ,metadata
        );

        log.info("Account: updateAccountBalance successful!");

        return result;
    }

    @Override
    public boolean isAccountOwnedByUser(long accountId, long userId) {
        return accountRepo.findByAccountIdAndUserId(accountId,userId)!=null;
    }

    @Override
    public boolean closeAllAccountsByUserId(long userId) {
        List<Account> accountList = accountRepo.findAllAccountsByUserId(userId);

        accountList.forEach(account -> {
            accountRepo.delete(account);

            if(accountRepo.existsById(userId)){
                throw new RuntimeException("Failed to delete the Account");
            }

            //kafka Notification
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("accountId",account.getAccountId());

            eventProducer.closeAllAccountEvent(
                    String.valueOf(userId)
                    ,metadata
            );

        });

        return true;
    }

    @Override
    public AccountStatusResponse getAccountStatus(long accountId, long userId) {
        Account account = accountRepo.findByAccountIdAndUserId(accountId,userId).orElseThrow(
                ()->new NoSuchElementException(String.format("Account not found for userId: %d and accountId: %d!",
                        userId,accountId))
        );
        return AccountStatusResponse.builder().accountStatus(account.getAccountStatus()).build();
    }

    @Override
    public Account accountExists(long accountId) {
        return accountRepo.findById(accountId)
                .orElseThrow(()->new RuntimeException(String.format("Account: Account not found for Account Id = %d",accountId)));
    }
}
