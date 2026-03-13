package com.loan_service.entity;

import com.loan_service.constant.LoanStatus;
import com.loan_service.constant.LoanType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "LOAN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Loan_Id",nullable = false,updatable = false,unique = true)
    private long loanId;

    @Column(name = "User_Id",nullable = false,updatable = false)
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Loan_Type", nullable = false)
    private LoanType loanType;

    @Column(name = "Loan_Amount", nullable = false)
    private BigDecimal loanAmount;

    @Column(name = "Tenure_Months", nullable = false)
    private int tenureMonth;

    @Column(name = "Interest_Rate",nullable = false)
    private BigDecimal interestRate;

    @CreatedDate
    @Column(name = "Application_Date",nullable = false,updatable = false)
    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Loan_Status",nullable = false)
    private LoanStatus loanStatus;

    @Column(name = "Approval_Date")
    private LocalDate approvalDate;

    @Column(name = "Status_Reason")
    private String statusReason;

    @LastModifiedDate
    @Column(name = "Updated_On",nullable = false)
    private LocalDate updatedOn;

    @PrePersist
    public void setDefault(){
        if(loanStatus==null){
            loanStatus=LoanStatus.PENDING;
            statusReason=LoanStatus.PENDING.toString();
        }

        if(interestRate==null){
            interestRate = loanType.getInterestRate();
        }

    }
}
