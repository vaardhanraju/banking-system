package org.example.bankingsystem.controller;

import org.example.bankingsystem.dto.ApiError;
import org.example.bankingsystem.dto.ApiResponse;
import org.example.bankingsystem.dto.request.CreateAccountRequest;
import org.example.bankingsystem.dto.request.DepositRequest;
import org.example.bankingsystem.dto.request.WithdrawRequest;
import org.example.bankingsystem.dto.response.CreateAccountResponse;
import org.example.bankingsystem.dto.response.DepositResponse;
import org.example.bankingsystem.dto.response.ViewAccountResponse;
import org.example.bankingsystem.dto.response.WithdrawResponse;
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
    public ResponseEntity<ApiResponse<DepositResponse>> deposit(@PathVariable String accountNumber, @RequestBody DepositRequest depositRequest) {

        if (accountNumber.length() != 13 || depositRequest.getAmount() <= 0) {
            ApiError error = new ApiError(
                    "INVALID_REQUEST", new ArrayList<>()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Invalid account number or amount", error));
        }


        Account account = accountService.deposit(accountNumber, depositRequest.getAmount());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(new DepositResponse(account.getBalance()),"Amount deposited successfully"));

    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponse<WithdrawResponse>> withdraw(@PathVariable String accountNumber, @RequestBody WithdrawRequest withdrawRequest) {

        if (accountNumber.length() != 13 || withdrawRequest.getAmount() <= 0) {
            ApiError error = new ApiError(
                    "INVALID_REQUEST", new ArrayList<>()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Invalid account number or amount", error));
        }

        Account account = accountService.withdraw(accountNumber, withdrawRequest.getAmount());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(new WithdrawResponse(account.getBalance()), "Amount withdrawn successfully."));
    }
}
