package com.api_gateway.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtTokenExpiredException extends RuntimeException{
    private final HttpStatus httpStatus=HttpStatus.UNAUTHORIZED;
    private String message;

    public JwtTokenExpiredException(String message){
        this.message=message;
    }
}
