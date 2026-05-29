package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CustomerDetailsResponse;
import org.platform.repair.entity.Customer;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.InvoiceRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.security.TenantContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RepairRepository repairRepository;
    private final InvoiceRepository invoiceRepository;

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }
    public List<Customer> search(String query) {
        Long shopId = TenantContext.get();
        return customerRepository.search(shopId, query);
    }
    public List<Customer> findAll() {
        Long shopId = TenantContext.get();

        return customerRepository.findByShopId(shopId);
    }
    public CustomerDetailsResponse getDetails(Long customerId) {

        Long shopId = TenantContext.get();

        Customer customer = customerRepository
                .findByIdAndShopId(customerId, shopId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<?> repairs =
                repairRepository.findByCustomerIdAndShopIdOrderByIdDesc(customerId, shopId);

        BigDecimal totalSpent =
                invoiceRepository.sumByCustomer(shopId, customerId);

        return new CustomerDetailsResponse(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                totalSpent,
                (long) repairs.size(),
                (List) repairs
        );
    }

}