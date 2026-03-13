package com.payment_service.entity;


import com.payment_service.constant.PaymentStatus;
import com.payment_service.constant.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENT")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Payment_Id", nullable = false, unique = true, updatable = false)
    private long paymentId;

    @Column(name = "User_Id", nullable = false, updatable = false)
    private long userId;

//    @Column(name="Transaction_Id", nullable = false, unique = true)
//    private String refTransactionId;

    @Column(name="Transaction_Id", unique = true)
    private String refTransactionId;

    @Column(name = "From_Account_Id", updatable = false, nullable = false)
    private long fromAccountId;

    @Column(name = "To_Account_Id", updatable = false, nullable = false)
    private long toAccountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Payment_Type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "Payment_Status", nullable = false)
    private PaymentStatus paymentStatus;

    @CreatedDate
    @Column(name = "Payment_Date", nullable = false, updatable = false)
    private LocalDateTime paymentDate;

    @Column(name = "Payment_Failure_Reason")
    private String failureReason;

    @Version
    private Long version;

}
