package com.authentication_service.entity;

import com.authentication_service.constant.Role;
import com.authentication_service.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "AUTHENTICATION_DETAILS")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Auth
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID",nullable = false, unique = true,updatable = false)
    private long userId;

    @Column(name = "USERNAME",unique = true,nullable = false)
    private String username;

    @Column(name = "EMAIL",unique = true,nullable = false)
    private String email;

    @Column(name = "PASSWORD",nullable = false)
    private String password;

    @Column(name = "ROLE",nullable = false)
    private Role role;

    @Column(name = "STATUS", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "CREATED_AT",updatable = false,nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT",nullable = false)
    private Instant updatedAt;

}
