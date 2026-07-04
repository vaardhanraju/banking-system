package org.example.bankingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.bankingsystem.enums.AccountStatus;
import org.example.bankingsystem.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ViewAccountResponse {
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private LocalDateTime dateCreated;
}
