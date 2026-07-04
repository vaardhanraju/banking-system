package org.example.bankingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankingsystem.enums.AccountStatus;
import org.example.bankingsystem.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountResponse {
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private LocalDateTime dateCreated;
}
