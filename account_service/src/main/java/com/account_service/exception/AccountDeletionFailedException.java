package com.account_service.exception;

public class AccountDeletionFailedException extends RuntimeException{
    private String message;

    public AccountDeletionFailedException(String message){
        super(message);
    }
}
