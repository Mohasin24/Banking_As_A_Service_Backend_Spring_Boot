package com.user_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.user_service.constant.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "USER_DETAILS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private long id;

    ///  Implement auto userid generation
    @Column(name = "USER_ID", nullable = false, unique = true)
    private long userId;

    @Column(name = "FIRST_NAME",nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false)
    private String lastName;

    @Column(name = "DATE_OF_BIRTH",nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Column(name = "ADDRESS",nullable = false)
    private String address;

    @Column(name = "PHONE_NO", unique = true, nullable = false)
    private String phoneNo;

    @Column(name = "KYC_STATUS")
    private KycStatus kycStatus;

    public String getFullName(){
        return firstName.strip() + " " + lastName.strip();
    }
}
