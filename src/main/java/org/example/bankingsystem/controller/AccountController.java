package org.example.bankingsystem.controller;

import org.example.bankingsystem.dto.request.CreateAccountRequest;
import org.example.bankingsystem.dto.response.ViewAccountResponse;
import org.example.bankingsystem.model.Account;
import org.example.bankingsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    public AccountService accountService;

    @PostMapping("/create/{customerID}")
    public ResponseEntity<?> createAccount(@PathVariable Integer customerID, @RequestBody CreateAccountRequest createAccountRequest) {
        Account account = accountService.addAccount(customerID, createAccountRequest);

        if (account == null) {
            return new ResponseEntity<>("Failed to create account", HttpStatus.BAD_REQUEST);
        }

        ViewAccountResponse viewAccountResponse = new ViewAccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getAccountStatus(),
                account.getDateCreated()
        );

        return new ResponseEntity<>(viewAccountResponse, HttpStatus.CREATED);
    }
}
