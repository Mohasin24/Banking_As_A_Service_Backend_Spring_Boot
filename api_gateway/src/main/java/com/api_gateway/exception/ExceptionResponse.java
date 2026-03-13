package com.api_gateway.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse
{
    private String message;
    private HttpStatus httpStatus;

    public ExceptionResponse(String message, HttpStatus httpStatus){
        this.message=message;
        this.httpStatus=httpStatus;
    }
}
