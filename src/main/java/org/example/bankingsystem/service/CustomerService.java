package org.example.bankingsystem.service;

import org.example.bankingsystem.dto.request.LoginRequest;
import org.example.bankingsystem.model.Customer;
import org.example.bankingsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    public CustomerRepository customerRepository;

    public Customer add(Customer customer) {
        return customerRepository.save(customer);
    }

    public boolean verifyCustomer(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return false;
        } else {
            return customer.getPassword().equals(password);
        }
    }
}
