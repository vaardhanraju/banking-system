package org.example.bankingsystem.repository;

import org.example.bankingsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountNumber(String generatedNumber);
}
