package com.transaction_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ExceptionResponse> invalidAmountException(InvalidAmountException e){

        log.warn("InvalidAmountException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST,e.getMessage()));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ExceptionResponse> webClientResponseException(WebClientResponseException e)
    {
        log.warn("WebClientResponseException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException e)
    {
        log.warn("IllegalArgumentException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST,e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> noSuchElementException(NoSuchElementException e)
    {
        log.warn("NoSuchElementException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpStatus.NOT_FOUND,e.getMessage()));
    }

    @ExceptionHandler(InsufficientAccountBalanceException.class)
    public ResponseEntity<ExceptionResponse> insufficientBalanceException(InsufficientAccountBalanceException e)
    {
        log.warn("InsufficientAccountBalanceException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ExceptionResponse(HttpStatus.CONFLICT,e.getMessage()));
    }

    @ExceptionHandler(AccountBalanceUpdateFailedException.class)
    public ResponseEntity<ExceptionResponse> accountBalanceUpdateFailedException(AccountBalanceUpdateFailedException e)
    {
        log.warn("AccountBalanceUpdateFailedException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()));
    }

    @ExceptionHandler(ReceiverAccountNotExistsException.class)
    public ResponseEntity<ExceptionResponse> receiverAccountNotExistsException(ReceiverAccountNotExistsException e){
        log.warn("ReceiverAccountNotExistsException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeException(RuntimeException e)
    {
        log.warn("RuntimeException exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> genericException(Exception e)
    {
        log.warn("Exception exception occurred: {}",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()));
    }

}
