package com.transaction_service.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import com.transaction_service.constant.TransactionStatus;
import com.transaction_service.constant.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTION")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {

    ///  implement auto generation of transaction id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",nullable = false)
    private long id;

    //Auto-generated
    @Column(name = "Transaction_Id", unique = true, nullable = false, updatable = false)
    private String transactionId;

    @Column(name = "USER_ID",nullable = false,updatable = false)
    private long userId;

    @Column(name="From_Account_Id",nullable = false,updatable = false)
    private long fromAccountId;

    @Column(name = "To_Account_Id", updatable = false, nullable = false)
    private long toAccountId;

    @Column(name = "AMOUNT",nullable = false,updatable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_TYPE",nullable = false,updatable = false)
    private TransactionType transactionType;

    // will contain string with Payment type details and account details
    @Column(name = "DESCRIPTION",nullable = false,updatable = false)
    private String description;

    @CreatedDate
    @Column(name="TRANSACTION_DATE",nullable = false,updatable = false)
    private LocalDateTime transactionDate;

    @Column(name = "Payment_Failure_Reason")
    private String failureReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "Payment_Status", nullable = false)
    private TransactionStatus transactionStatus;

    @Version
    private Long version;

    @PrePersist
    private void prePersist(){
        this.transactionId = UlidCreator.getUlid().toString();
    }

}
