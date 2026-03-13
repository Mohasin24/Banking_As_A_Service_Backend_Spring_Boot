package com.account_service.dto;

import com.account_service.constant.AccountStatus;
import lombok.Builder;

@Builder
public record AccountStatusResponse(AccountStatus accountStatus) {
}
