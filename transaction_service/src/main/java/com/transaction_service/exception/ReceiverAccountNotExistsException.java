package com.transaction_service.exception;

import lombok.Getter;

@Getter
public class ReceiverAccountNotExistsException extends RuntimeException {

  private String message = "Receiver Account not exists!";

  public ReceiverAccountNotExistsException(){}

  public ReceiverAccountNotExistsException(String message) {
    this.message=message;
  }
}
