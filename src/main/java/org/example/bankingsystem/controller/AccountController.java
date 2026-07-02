package org.example.bankingsystem.controller;

import org.example.bankingsystem.dto.request.CreateAccountRequest;
import org.example.bankingsystem.dto.request.DepositRequest;
import org.example.bankingsystem.dto.request.WithdrawRequest;
import org.example.bankingsystem.dto.response.CreateAccountResponse;
import org.example.bankingsystem.dto.response.DepositResponse;
import org.example.bankingsystem.dto.response.ViewAccountResponse;
import org.example.bankingsystem.dto.response.WithdrawResponse;
import org.example.bankingsystem.exceptions.AccountNotFoundException;
import org.example.bankingsystem.exceptions.InsufficientBalanceException;
import org.example.bankingsystem.model.Account;
import org.example.bankingsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    public AccountService accountService;

    @PostMapping("/create/{customerID}")
    public ResponseEntity<CreateAccountResponse> createAccount(@PathVariable Integer customerID, @RequestBody CreateAccountRequest createAccountRequest) {
        Account account = accountService.addAccount(customerID, createAccountRequest);

        if (account == null) {
            return new ResponseEntity<>(new CreateAccountResponse(
                    "FAILED", "Failed to create account"
            ), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new CreateAccountResponse(
                "SUCCESS", "Account created successfully"
        ), HttpStatus.CREATED);
    }

    @GetMapping("/view/{customerID}")
    public ResponseEntity<?> viewAccounts(@PathVariable Integer customerID) {
        List<Account> accounts = accountService.findAccountByCustomerId(customerID);

        if (accounts == null) {
            return new ResponseEntity<>("No accounts associated with this ID", HttpStatus.NOT_FOUND);
        }

        List<ViewAccountResponse> viewAccountResponseList = new ArrayList<>();

        for (Account account: accounts) {
            ViewAccountResponse viewAccountResponse = new ViewAccountResponse(
                    account.getAccountNumber(),
                    account.getBalance(),
                    account.getAccountType(),
                    account.getAccountStatus(),
                    account.getDateCreated()
            );
            viewAccountResponseList.add(viewAccountResponse);
        }

        return new ResponseEntity<>(viewAccountResponseList, HttpStatus.OK);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<DepositResponse> deposit(@PathVariable String accountNumber, @RequestBody DepositRequest depositRequest) {

        if (accountNumber.length() != 13 || depositRequest.getAmount() <= 0) {
            return new ResponseEntity<>(new DepositResponse(0.0, "Invalid account number or amount"), HttpStatus.BAD_REQUEST);
        }

        try {
            Account account = accountService.deposit(accountNumber, depositRequest.getAmount());
            return new ResponseEntity<>(new DepositResponse(
             account.getBalance(), "Amount deposited successfully"
            ), HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new DepositResponse(0.0, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(@PathVariable String accountNumber, @RequestBody WithdrawRequest withdrawRequest) {

        if (accountNumber.length() != 13 || withdrawRequest.getAmount() <= 0) {
            return new ResponseEntity<>(new WithdrawResponse("Invalid account number or amount"), HttpStatus.BAD_REQUEST);
        }

        try {
            Account account = accountService.withdraw(accountNumber, withdrawRequest.getAmount());
            return new ResponseEntity<>(new WithdrawResponse("Amount withdrawn successfully. Balance: " + account.getBalance()), HttpStatus.OK);
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            return new ResponseEntity<>(new WithdrawResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
