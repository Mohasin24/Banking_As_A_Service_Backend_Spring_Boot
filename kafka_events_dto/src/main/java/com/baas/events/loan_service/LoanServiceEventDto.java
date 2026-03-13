package com.baas.events.loan_service;

import com.baas.constants.loan_service.LoanServiceEventType;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanServiceEventDto
{
    private LoanServiceEventType loanServiceEventType;
    private String userId;
    private String message;
    private Map<String, Object> metadata;
}
