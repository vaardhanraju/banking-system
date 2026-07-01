package org.example.bankingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private Integer id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
}
