package com.account_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

 @RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountDeletionFailedException.class)
    public ResponseEntity<ExceptionResponse> failedAccountDeletionException(AccountDeletionFailedException e){
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(
                        new ExceptionResponse(
                                HttpStatus.NOT_IMPLEMENTED,
                                e.getMessage()
                        )
                );
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> noSuchElementException(NoSuchElementException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ExceptionResponse(
                                HttpStatus.NOT_FOUND,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> nullPointerException(NullPointerException e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ExceptionResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ExceptionResponse(
                                HttpStatus.BAD_REQUEST,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ExceptionResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> illegalStateException(IllegalStateException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ExceptionResponse(
                                HttpStatus.BAD_REQUEST,
                                e.getMessage()
                        )
                );
    }
}
