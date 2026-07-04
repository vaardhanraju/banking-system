package org.example.bankingsystem.controller;

import org.example.bankingsystem.dto.ApiError;
import org.example.bankingsystem.dto.ApiResponse;
import org.example.bankingsystem.dto.response.LoginResponse;
import org.example.bankingsystem.dto.request.LoginRequest;
import org.example.bankingsystem.dto.request.SignupRequest;
import org.example.bankingsystem.dto.response.SignupResponse;
import org.example.bankingsystem.model.Customer;
import org.example.bankingsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    public CustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> createCustomer(@RequestBody SignupRequest signupRequest) {
        Customer customer = new Customer();
        customer.setName(signupRequest.getName());
        customer.setEmail(signupRequest.getEmail());
        customer.setDateOfBirth(signupRequest.getDateOfBirth());
        customer.setAddress(signupRequest.getAddress());
        customer.setPassword(signupRequest.getPassword());

        Customer customerDb = customerService.add(customer);
        SignupResponse signupResponse = new SignupResponse(
                customerDb.getId(), customerDb.getName(), customerDb.getEmail(), customerDb.getDateOfBirth(), customerDb.getAddress()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(signupResponse, "Account created successfully"));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginCustomer(@RequestBody LoginRequest loginRequest) {
        if (customerService.verifyCustomer(loginRequest)) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("INVALID_CREDENTIALS", new ApiError("Invalid password", null)));
        }
    }

}
