package com.loan_service.constant;

import java.math.BigDecimal;

public enum LoanType
{
    PERSONAL_LOAN(BigDecimal.valueOf(10.5)),
    HOME_LOAN(BigDecimal.valueOf(8.3)),
    CAR_LOAN(BigDecimal.valueOf(8.7)),
    BIKE_LOAN(BigDecimal.valueOf(9.5)),
    GOLD_LOAN(BigDecimal.valueOf(9.0)),
    EDUCATIONAL_LOAN(BigDecimal.valueOf(9.0));

    private final BigDecimal interestRate;

    LoanType(BigDecimal interestRate){
        this.interestRate=interestRate;
    }

    public BigDecimal getInterestRate()
    {
        return interestRate;
    }
}
