package org.example.bankingsystem.controller;

import org.example.bankingsystem.dto.SignupRequest;
import org.example.bankingsystem.dto.SignupResponse;
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
@RequestMapping("/user")
public class CustomerController {

    @Autowired
    public CustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> createCustomer(@RequestBody SignupRequest signupRequest) {
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
        return new ResponseEntity<>(signupResponse, HttpStatus.CREATED);
    }
}
