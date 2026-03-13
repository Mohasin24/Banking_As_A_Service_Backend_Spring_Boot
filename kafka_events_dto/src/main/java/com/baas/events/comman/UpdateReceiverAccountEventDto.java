package com.baas.events.comman;


import com.baas.constants.comman.CommanEventType;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateReceiverAccountEventDto {
    private CommanEventType commanEventType;
    private long accountId;
    private long receiverUserId;
    private BigDecimal amount;
}
