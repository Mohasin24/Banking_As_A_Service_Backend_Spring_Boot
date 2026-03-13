package com.account_service.entity;

import com.account_service.constant.AccountStatus;
import com.account_service.constant.AccountType;
import com.account_service.constant.SystemAccountType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ACCOUNT")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account
{
    /// implement auto account id generation
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ACCOUNT_ID", nullable = false,updatable = false, unique = true)
    private Long accountId;

    @Column(name="USER_ID",updatable = false)
    private Long userId;

    @Column(name = "ACCOUNT_NO",nullable = false,updatable = false,unique = true)
    private String accountNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE", nullable = false,updatable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "SYSTEM_TYPE", updatable = false)
    private SystemAccountType systemAccountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_STATUS", nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "ACCOUNT_BALANCE",nullable = false)
    private BigDecimal accountBalance;

    @CreatedDate
    @Column(name="CREATED_AT",nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name="UPDATED_AT",nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;
}
