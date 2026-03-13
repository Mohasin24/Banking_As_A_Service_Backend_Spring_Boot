package com.user_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;

@Builder
public record UserProfileUpdateRequest(String firstName, String lastName, @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dob, String address, String phoneNo) {
}
