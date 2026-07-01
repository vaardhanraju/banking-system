package org.example.bankingsystem.utils;

import org.example.bankingsystem.model.Account;
import org.example.bankingsystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AccountNumberGenerator {

    @Autowired
    public AccountRepository accountRepository;

    public String generateNumber() {
        int maxAttempts = 10;
        int attempts = 0;

        while (attempts < maxAttempts) {
            String generatedNumber = "ACC0305" + sixDigit();
            if (!validateAccountNumber(generatedNumber)) {
                return generatedNumber;
            }
            attempts++;
        }
        throw new RuntimeException("Could not generate unique account number");
    }

    public String sixDigit() {
        int rawNumber = ThreadLocalRandom.current().nextInt(0, 1000000);
        return String.format("%06d", rawNumber);
    }

    public boolean validateAccountNumber(String generatedNumber) {
        Account account = accountRepository.findByAccountNumber(generatedNumber).orElse(null);
        return account != null;
    }
}
