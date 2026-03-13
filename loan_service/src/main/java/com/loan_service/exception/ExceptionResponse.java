package com.loan_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

    private final HttpStatus httpStatus;
    private final String message;

    public ExceptionResponse(HttpStatus httpStatus, String message){
        this.httpStatus=httpStatus;
        this.message=message;
    }
}
