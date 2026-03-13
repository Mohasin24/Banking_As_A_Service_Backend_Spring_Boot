package com.loan_service.repository;

import com.loan_service.constant.LoanStatus;
import com.loan_service.dto.LoanStatusRequest;
import com.loan_service.entity.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {

    List<Loan> findAllByUserId(long userId);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE Loan l SET l.loanStatus = :loanStatus, l.statusReason= :statusReason, l.approvalDate = :approvalDate WHERE l.loanId= :loanId " +
                "AND l.userId= :userId"
    )
    int updateLoanStatus(
            @Param("loanId") long loanID,
            @Param("userId") long userId,
            @Param("loanStatus") LoanStatus loanStatus,
            @Param("statusReason") String statusReason,
            @Param("approvalDate") LocalDate approvalDate
    );
}
