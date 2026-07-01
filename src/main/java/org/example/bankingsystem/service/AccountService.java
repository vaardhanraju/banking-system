package org.example.bankingsystem.service;

import org.example.bankingsystem.dto.request.CreateAccountRequest;
import org.example.bankingsystem.enums.AccountStatus;
import org.example.bankingsystem.model.Account;
import org.example.bankingsystem.model.Customer;
import org.example.bankingsystem.repository.AccountRepository;
import org.example.bankingsystem.repository.CustomerRepository;
import org.example.bankingsystem.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        if (accountRequest.getBalance() <= 0) {
            return null;
        }

        Customer customer = customerRepository.findById(customerID).orElse(null);
        if (customer == null) {
            return null;
        }

        String generatedAccountNumber = accountNumberGenerator.generateNumber();

        Account account = new Account();
        account.setAccountNumber(generatedAccountNumber);
        account.setBalance(accountRequest.getBalance());
        account.setAccountType(accountRequest.getAccountType());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);
        account.setDateCreated(LocalDate.now());

        return accountRepository.save(account);
    }

    public List<Account> findAccountByCustomerId(Integer customerID) {
        return accountRepository.findByCustomerId(customerID);
    }
}
