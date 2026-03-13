package com.loan_service.servicesImpl;

import com.loan_service.constant.LoanType;
import com.loan_service.dto.LoanApplicationRequest;
import com.loan_service.dto.LoanApplicationResponse;
import com.loan_service.dto.LoanStatusRequest;
import com.loan_service.dto.LoanStatusResponse;
import com.loan_service.entity.Loan;
import com.loan_service.kafka.LoanEventProducer;
import com.loan_service.repository.LoanRepo;
import com.loan_service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepo loanRepo;
    private final LoanEventProducer loanEventProducer;

    @Autowired
    public LoanServiceImpl(LoanRepo loanRepo,LoanEventProducer loanEventProducer){
        this.loanRepo=loanRepo;
        this.loanEventProducer=loanEventProducer;
    }

    @Override
    public LoanApplicationResponse applyLoan(LoanApplicationRequest loanApplicationRequest) {

        Loan loan = Loan.builder()
                .userId(loanApplicationRequest.userId())
                .loanType(LoanType.valueOf(loanApplicationRequest.loanType()))
                .loanAmount(loanApplicationRequest.loanAmount())
                .tenureMonth(loanApplicationRequest.tenureMonth())
                .build();

        Loan savedLoanApplication = loanRepo.save(loan);

        //Kafka Events
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId",savedLoanApplication.getUserId());
        metadata.put("loanId",savedLoanApplication.getLoanId());
        metadata.put("loanType",savedLoanApplication.getLoanType().toString());
        metadata.put("loanAmount",savedLoanApplication.getLoanAmount());
        metadata.put("tenureMonth",savedLoanApplication.getTenureMonth());

        loanEventProducer.loanApplicationEvent(String.valueOf(loan.getUserId()),metadata);

        return mapToLoanApplicationResponse(savedLoanApplication);
    }

    @Override
    public LoanApplicationResponse getLoan(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow(
                ()->new RuntimeException(String.format(
                        "Loan Application not found for id:%d"
                        ,loanId
                ))
        );

        return mapToLoanApplicationResponse(loan);
    }

    @Override
    public List<LoanApplicationResponse> getUserLoans(Long userId) {

        List<Loan> loans = loanRepo.findAllByUserId(userId);

        return loans.stream().map(this::mapToLoanApplicationResponse).toList();
    }

    @Override
    public LoanStatusResponse updateLoanStatus(LoanStatusRequest loanStatusRequest) {

        // first check if the user id is present or not in the user section

        int loanUpdated = loanRepo.updateLoanStatus(
                loanStatusRequest.loanId(),
                loanStatusRequest.userId(),
                loanStatusRequest.loanStatus(),
                loanStatusRequest.statusReason(),
                loanStatusRequest.approvalDate()
        );

        Loan loan = loanRepo.findById(loanStatusRequest.loanId()).get();

        //Kafka Event check approval status
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("loanId",loan.getLoanId());
        metadata.put("userId",loan.getUserId());
        metadata.put("loanStatus",loan.getLoanStatus().toString());
        metadata.put("statusReason",loan.getStatusReason());

        loanEventProducer.loanApprovalStatusEvent(String.valueOf(loan.getUserId()),metadata);

        return mapToLoanStatusResponse(loan);
    }

    @Override
    public LoanStatusResponse getLoanStatus(Long loanId) {

        Loan loan = loanRepo.findById(loanId).orElseThrow(()->new RuntimeException(String.format(
                "Loan Application not found for id:%d"
                ,loanId
        )));

        return mapToLoanStatusResponse(loan);
    }


    public LoanApplicationResponse mapToLoanApplicationResponse(Loan loan){
        return LoanApplicationResponse.builder()
                .loanId(loan.getLoanId())
                .userId(loan.getUserId())
                .loanType(loan.getLoanType().name())
                .loanAmount(loan.getLoanAmount())
                .interestRate(loan.getInterestRate())
                .applicationDate(loan.getApplicationDate())
                .tenureMonth(loan.getTenureMonth())
                .loanStatus(loan.getLoanStatus())
                .approvalDate(loan.getApprovalDate())
                .statusReason(loan.getStatusReason())
                .updatedOn(loan.getUpdatedOn())
                .build();

    }

    public LoanStatusResponse mapToLoanStatusResponse(Loan loan){
        return LoanStatusResponse.builder()
                .loanId(loan.getLoanId())
                .userId(loan.getUserId())
                .loanStatus(loan.getLoanStatus())
                .statusReason(loan.getStatusReason())
                .approvalDate(loan.getApprovalDate())
                .build();
    }
}
