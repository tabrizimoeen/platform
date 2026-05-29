package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CustomerDetailsResponse;
import org.platform.repair.entity.Customer;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.security.TenantContext;
import org.platform.repair.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }
    @GetMapping("/{id}")
    public CustomerDetailsResponse getDetails(@PathVariable Long id) {
        return customerService.getDetails(id);
    }
    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/search")
    public List<Customer> search(@RequestParam String q) {

        return customerService.search(q);
    }

}