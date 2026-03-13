package com.loan_service.kafka;

import com.baas.constants.loan_service.LoanServiceEventType;
import com.baas.events.loan_service.LoanServiceEventDto;
import com.baas.topics.KafkaTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoanEventProducer {

    private final KafkaTemplate<String, LoanServiceEventDto> kafkaTemplate;
    private final String TOPIC = KafkaTopics.LOAN_SERVICE_EVENT;

    @Autowired
    public LoanEventProducer(KafkaTemplate<String, LoanServiceEventDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void loanApplicationEvent(String userId, Map<String, Object> metadata){
        String html = """
                        <html>
                            <body>
                                <h4>New Loan Application</h2>
                                <p><strong>Loan Id:</strong> %d</p>
                                <p><strong>User Id:</strong> %s</p>
                                <p><strong>Loan Type:</strong> %s</p>
                                <p><strong>Loan Amount:</strong> %s</p>
                                <p><strong>Tenure Month:</strong> %d</p>
                            </body>
                        </html>
                """;

        String body = html.formatted(
                metadata.get("loanId"),
                metadata.get("userId"),
                metadata.get("loanType"),
                metadata.get("loanAmount"),
                metadata.get("tenureMonth")
        );


        LoanServiceEventDto loanServiceEventDto = LoanServiceEventDto.builder()
                .loanServiceEventType(LoanServiceEventType.LOAN_APPLICATION)
                .userId(userId)
                .message(body)
                .metadata(metadata)
                .build();
        kafkaTemplate.send(TOPIC,userId,loanServiceEventDto);
    }

    public void loanApprovalStatusEvent(String userId, Map<String, Object> metadata){
        String html = """
                        <html>
                            <body>
                                <h4>Loan Approval Status: %s</h2>
                                <p><strong>Loan Id:</strong> %d</p>
                                <p><strong>User Id:</strong> %d</p>
                                <p><strong>Loan Status:</strong> %s</p>
                                <p><strong>Status Reason:</strong> %s</p>
                            </body>
                        </html>
                        """;
        String body = html.formatted(
                metadata.get("loanStatus"),
                metadata.get("loanId"),
                metadata.get("userId"),
                metadata.get("loanStatus"),
                metadata.get("statusReason")
        );

        LoanServiceEventDto loanServiceEventDto = LoanServiceEventDto.builder()
                .loanServiceEventType(LoanServiceEventType.LOAN_APPROVAL_STATUS)
                .userId(userId)
                .message(body)
                .metadata(metadata)
                .build();
        kafkaTemplate.send(TOPIC,userId,loanServiceEventDto);
    }


}
