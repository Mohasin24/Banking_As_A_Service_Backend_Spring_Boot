package com.transaction_service.exception;

public class InvalidAmountException extends RuntimeException{
    private String message = "Invalid amount! Please enter valid amount.";

    public InvalidAmountException(){}

    public InvalidAmountException(String message){
        this.message=message;
    }
}
