package com.loan_service.service;

import com.loan_service.dto.*;

import java.util.List;

public interface LoanService {

    LoanApplicationResponse applyLoan(LoanApplicationRequest request);

    LoanApplicationResponse getLoan(Long loanId);

    List<LoanApplicationResponse> getUserLoans(Long userId);

LoanStatusResponse updateLoanStatus(LoanStatusRequest loanStatusRequest);

    LoanStatusResponse getLoanStatus(Long loanId);

}
