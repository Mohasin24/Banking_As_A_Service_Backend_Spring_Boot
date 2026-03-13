package com.transaction_service.exception;

import lombok.Getter;

@Getter
public class AccountBalanceUpdateFailedException extends RuntimeException{
    private String message="Transaction Failed! Failed to update the account balance.";

    public AccountBalanceUpdateFailedException(){}

    public AccountBalanceUpdateFailedException(String message){
        this.message=message;
    }
}
