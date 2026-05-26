package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.entity.Customer;
import org.platform.repair.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    public Customer create(Customer customer) {
        return repository.save(customer);
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }
}