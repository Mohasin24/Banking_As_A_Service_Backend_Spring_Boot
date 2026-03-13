package com.loan_service.controller;

import com.loan_service.constant.LoanStatus;
import com.loan_service.dto.LoanApplicationRequest;
import com.loan_service.dto.LoanApplicationResponse;
import com.loan_service.dto.LoanStatusRequest;
import com.loan_service.dto.LoanStatusResponse;
import com.loan_service.entity.Loan;
import com.loan_service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loan")
public class LoanController
{
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService){
        this.loanService=loanService;
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Get Mapping                                //
    ////////////////////////////////////////////////////////////////////////

    @GetMapping("/loan-details/{loan-id}")
    public ResponseEntity<LoanApplicationResponse> getLoanDetails(@PathVariable("loan-id") long loanId)
    {
        return ResponseEntity.ok(loanService.getLoan(loanId));
    }

    @GetMapping("/all-loans/{user-id}")
    public ResponseEntity<List<LoanApplicationResponse>> getAllLoanDetails(@PathVariable("user-id") long userId){
        return ResponseEntity.ok(loanService.getUserLoans(userId));
    }

    @GetMapping("/loan-status/{loan-id}")
    public ResponseEntity<LoanStatusResponse> getLoanStatus(@PathVariable("loan-id") long loanId){
        return ResponseEntity.ok(loanService.getLoanStatus(loanId));
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Post Mapping                               //
    ////////////////////////////////////////////////////////////////////////

    @PostMapping("/apply-loan")
    public ResponseEntity<LoanApplicationResponse> applyLoan(@RequestBody LoanApplicationRequest loanApplicationRequest)
    {
        return ResponseEntity.ok(loanService.applyLoan(loanApplicationRequest));
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Patch Mapping                              //
    ////////////////////////////////////////////////////////////////////////

    @PatchMapping("/approve/{user-id}/{loan-id}")
    public ResponseEntity<?> approveLoan(
            @PathVariable("loan-id") long loanId,
            @PathVariable("user-id") long userId,
            @RequestParam(name = "comments",required = false) String comments
            )
    {
        LoanStatusRequest loanStatusRequest=LoanStatusRequest.builder()
                .loanId(loanId)
                .userId(userId)
                .loanStatus(LoanStatus.APPROVED)
                .statusReason(comments)
                .approvalDate(LocalDate.now())
                .build();

        return ResponseEntity.ok(loanService.updateLoanStatus(loanStatusRequest));
    }

    @PatchMapping("/reject/{user-id}/{loan-id}")
    public ResponseEntity<?> rejectLoan(
            @PathVariable("loan-id") long loanId,
            @PathVariable("user-id") long userId,
            @RequestParam("comments") String comments
    ){
        LoanStatusRequest loanStatusRequest=LoanStatusRequest.builder()
                .loanId(loanId)
                .userId(userId)
                .statusReason(comments)
                .loanStatus(LoanStatus.REJECTED)
                .approvalDate(LocalDate.now())
                .build();

        return ResponseEntity.ok(loanService.updateLoanStatus(loanStatusRequest));
    }

    ////////////////////////////////////////////////////////////////////////
    //                         Delete Mapping                             //
    ////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    //                           Test Mapping                             //
    ////////////////////////////////////////////////////////////////////////
    @GetMapping("/test")
    public String test(){
        return "LoanController controller";
    }
}
