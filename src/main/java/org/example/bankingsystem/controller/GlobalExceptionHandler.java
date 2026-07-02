package org.example.bankingsystem.controller;

import org.example.bankingsystem.dto.ApiError;
import org.example.bankingsystem.dto.ApiResponse;
import org.example.bankingsystem.exceptions.AccountNotFoundException;
import org.example.bankingsystem.exceptions.InsufficientBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException exception) {
        ApiError error = new ApiError("ACCOUNT_NOT_FOUND", List.of(exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Account not found", error));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException exception) {
        ApiError error = new ApiError("INSUFFICIENT_FUNDS", List.of(exception.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Insufficient funds", error));
    }
}
