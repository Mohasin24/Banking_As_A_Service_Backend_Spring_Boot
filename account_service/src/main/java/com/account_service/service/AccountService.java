package com.account_service.service;

import com.account_service.dto.AccountRequest;
import com.account_service.dto.AccountResponse;
import com.account_service.dto.AccountStatusResponse;
import com.account_service.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountResponse openAccount(AccountRequest request);

    boolean closeAccount(long accountId,long userId);

    AccountResponse getAccount(long accountId, long userId);

    List<AccountResponse> getAllAccountsByUserId(long userId);

    boolean freezeAccount(long accountId, long userId);

    boolean unfreezeAccount(long accountId, long userId);

    BigDecimal getAccountBalance(long accountId, long userId);

    int updateAccountBalance(long accountId, long userId, BigDecimal amount);

    boolean isAccountOwnedByUser(long accountId, long userId);

    boolean closeAllAccountsByUserId(long userId);

    AccountStatusResponse getAccountStatus(long accountId, long userId);

    Account accountExists(long accountId);
    //    List<TransactionResponse> getAccountStatement(long accountId, long userId, LocalDate from, LocalDate to);
    //    AccountResponse updateAccount(long accountId, long userId, AccountUpdateRequest request);
}
