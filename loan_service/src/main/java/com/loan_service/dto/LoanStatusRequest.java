package com.loan_service.dto;

import com.loan_service.constant.LoanStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LoanStatusRequest
        (
                long loanId,
                long userId,
                LoanStatus loanStatus,
                String statusReason,
                LocalDate approvalDate
        ) {
}
