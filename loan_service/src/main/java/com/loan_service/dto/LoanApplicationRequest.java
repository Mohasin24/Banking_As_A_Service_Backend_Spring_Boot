package com.loan_service.dto;

import java.math.BigDecimal;

public record LoanApplicationRequest(long userId, String loanType, BigDecimal loanAmount, int tenureMonth) {
}
