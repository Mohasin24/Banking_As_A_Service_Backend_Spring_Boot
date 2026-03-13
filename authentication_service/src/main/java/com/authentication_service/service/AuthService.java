package com.authentication_service.service;

import com.authentication_service.dto.LoginDto;
import com.authentication_service.dto.LoginResponseDto;
import com.authentication_service.dto.RegisterDto;
import com.authentication_service.dto.RegisterResponseDto;
import com.authentication_service.entity.Auth;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    RegisterResponseDto register(RegisterDto registerDto);

    LoginResponseDto validateLogin(LoginDto loginDto);

    String updateEmail(long userId,String email);

    String updatePassword(long userId,String password);

    String updateStatus(long userId,String status);
}
