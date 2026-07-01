package org.example.bankingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.bankingsystem.enums.AccountStatus;
import org.example.bankingsystem.enums.AccountType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ViewAccountResponse {
    private String accountNumber;
    private Double balance;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private LocalDate dateCreated;
}
