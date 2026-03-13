package com.user_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {
    private HttpStatus httpStatus;
    private String message;

    public ExceptionResponse(HttpStatus httpStatus, String message){
        this.httpStatus=httpStatus;
        this.message=message;
    }
}
