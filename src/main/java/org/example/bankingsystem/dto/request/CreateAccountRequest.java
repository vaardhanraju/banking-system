package org.example.bankingsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankingsystem.enums.AccountType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    private AccountType accountType;
    private Double balance;
}
