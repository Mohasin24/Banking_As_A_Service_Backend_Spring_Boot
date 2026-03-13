package com.loan_service.dto;

import com.loan_service.constant.LoanStatus;
import com.loan_service.constant.LoanType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record LoanApplicationResponse(
        long loanId,
        long userId,
        String loanType,
        BigDecimal loanAmount,
        BigDecimal interestRate,
        LocalDate applicationDate,
        int tenureMonth,
        LoanStatus loanStatus,
        LocalDate approvalDate,
        String statusReason,
        LocalDate updatedOn
) {
}
