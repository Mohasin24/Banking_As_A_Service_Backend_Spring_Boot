package com.user_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;

@Builder
public record UserProfileRequest(long userId, String firstName, String lastName, @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dob,
        String address,
                                 String phoneNo) {
}
