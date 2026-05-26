package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.entity.Customer;
import org.platform.repair.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return service.create(customer);
    }

    @GetMapping
    public List<Customer> findAll() {
        return service.findAll();
    }
}