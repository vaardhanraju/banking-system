package org.example.bankingsystem.service;

import org.example.bankingsystem.dto.request.CreateAccountRequest;
import org.example.bankingsystem.enums.AccountStatus;
import org.example.bankingsystem.exceptions.AccountNotFoundException;
import org.example.bankingsystem.exceptions.CustomerNotFoundException;
import org.example.bankingsystem.exceptions.InsufficientBalanceException;
import org.example.bankingsystem.exceptions.InvalidAccountRequestException;
import org.example.bankingsystem.model.Account;
import org.example.bankingsystem.model.Customer;
import org.example.bankingsystem.repository.AccountRepository;
import org.example.bankingsystem.repository.CustomerRepository;
import org.example.bankingsystem.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    public AccountNumberGenerator accountNumberGenerator;

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    public AccountRepository accountRepository;

    public Account addAccount(Integer customerID, CreateAccountRequest accountRequest) {
        if (accountRequest.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAccountRequestException("Initial balance must be greater than 0");
        }

        Customer customer = customerRepository.findById(customerID).orElseThrow(
                () -> new CustomerNotFoundException("No customer associated with this ID")
        );

        String generatedAccountNumber = accountNumberGenerator.generateNumber();

        Account account = new Account();
        account.setAccountNumber(generatedAccountNumber);
        account.setBalance(accountRequest.getBalance());
        account.setAccountType(accountRequest.getAccountType());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);
        account.setDateCreated(LocalDateTime.now());

        return accountRepository.save(account);
    }

    public List<Account> findAccountByCustomerId(Integer customerID) {
        List<Account> accounts = accountRepository.findByCustomerId(customerID);

        if (accounts.isEmpty())
            throw new AccountNotFoundException("No associated accounts found with customer ID: " + customerID);

        return accounts;
    }

    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist: " + accountNumber));

        BigDecimal existingBalance = account.getBalance();
        account.setBalance(existingBalance.add(amount));

        accountRepository.save(account);

        return account;
    }

    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist: " + accountNumber));

        BigDecimal existingBalance = account.getBalance();

        if (existingBalance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Balance: " + existingBalance);
        }

        account.setBalance(existingBalance.subtract(amount));

        accountRepository.save(account);

        return account;
    }
}
