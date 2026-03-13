package com.account_service.repository;

import com.account_service.constant.AccountType;
import com.account_service.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository< Account,Long> {

    Optional<Account> findByAccountIdAndUserId(long accountId, long userId);
    List<Account> findAllAccountsByUserId(long userId);
    Optional<Account> findByAccountType(AccountType accountType);
    List<Account> findAllByAccountType(AccountType accountType);

    @Query(
            value = "SELECT a.accountBalance FROM Account a WHERE a.accountId = :accountId AND a.userId= :userId"
    )
    BigDecimal getAccountBalanceByAccountIdAndUserId(
            @Param("accountId") long accountId,
            @Param("userId") long userId
    );

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Account a SET a.accountBalance = :balance, a.updatedAt=CURRENT_TIMESTAMP WHERE a.accountId = :accountId AND a.userId= :userId"
    )
    int updateAccountBalance(
            @Param("accountId") long accountId,
            @Param("userId") long userId,
            @Param("balance") BigDecimal amount
    );
}
