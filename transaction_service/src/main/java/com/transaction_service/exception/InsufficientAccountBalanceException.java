package com.transaction_service.exception;

import lombok.Getter;

@Getter
public class InsufficientAccountBalanceException extends RuntimeException{
    private String message = "Insufficient Account Balance!";

    public InsufficientAccountBalanceException(){}

    public InsufficientAccountBalanceException(String message){
        this.message=message;
    }
}
