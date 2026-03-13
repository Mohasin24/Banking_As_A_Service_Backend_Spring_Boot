package com.account_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name="PROCESSED_EVENTS ")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessedEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    //Created by the producer and its unique
    @Column(name = "EVENT_ID", nullable = false, unique = true,updatable = false)
    private String eventId;

    @Column(name = "USER_ID", nullable = false,updatable = false)
    private String userId;

    @Column(name = "TRANSACTION_ID", nullable = false,updatable = false)
    private String transactionId;

    @CreatedDate
    @Column(name="CREATED_AT",nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name="UPDATED_AT",nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;
}
