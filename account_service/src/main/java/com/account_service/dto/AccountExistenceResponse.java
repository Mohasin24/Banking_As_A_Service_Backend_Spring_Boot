package com.account_service.dto;

import com.account_service.constant.AccountStatus;
import com.account_service.constant.AccountType;
import lombok.Builder;

@Builder
public record AccountExistenceResponse(
        long accountId,
        long userId,
        String accountNo,
        String accountType,
        String accountStatus
) {
}
