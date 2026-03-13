package com.payment_service.repository;

import com.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {

    @Query(
            value = "SELECT p FROM Payment p where p.paymentId = :paymentId AND p.userId= :userId"
    )
    Payment findByPaymentIdAndUserId(@Param("paymentId") long paymentId, @Param("userId") long userId);

    String findPaymentStatusByPaymentId(long paymentId);

}
