package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CustomerDetailsResponse;
import org.platform.repair.entity.Customer;
import org.platform.repair.entity.RepairShop;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.InvoiceRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.repository.RepairShopRepository;
import org.platform.repair.security.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RepairRepository repairRepository;
    private final InvoiceRepository invoiceRepository;
    private final RepairShopRepository repairShopRepository;

    @Transactional
    public Customer create(Customer incoming) {
        if (incoming.getName() == null || incoming.getName().isBlank()) {
            throw new RuntimeException("نام مشتری الزامی است");
        }
        if (incoming.getPhone() == null || incoming.getPhone().isBlank()) {
            throw new RuntimeException("شماره مشتری الزامی است");
        }

        Long shopId = TenantContext.get();
        if (shopId == null) {
            throw new RuntimeException("تعمیرگاه کاربر مشخص نیست");
        }
        RepairShop shop = repairShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("تعمیرگاه پیدا نشد"));
        String phone = incoming.getPhone().trim();

        if (incoming.getId() != null) {
            Customer existing = customerRepository.findByIdAndShopId(incoming.getId(), shopId)
                    .orElseThrow(() -> new RuntimeException("مشتری پیدا نشد"));
            customerRepository.findByPhoneAndShopId(phone, shopId)
                    .filter(other -> !other.getId().equals(existing.getId()))
                    .ifPresent(other -> {
                        throw new RuntimeException("این شماره برای مشتری دیگری ثبت شده است");
                    });
            existing.setName(incoming.getName().trim());
            existing.setPhone(phone);
            return customerRepository.save(existing);
        }

        if (customerRepository.findByPhoneAndShopId(phone, shopId).isPresent()) {
            throw new RuntimeException("این شماره قبلاً ثبت شده است");
        }

        Customer customer = new Customer();
        customer.setName(incoming.getName().trim());
        customer.setPhone(phone);
        customer.setShop(shop);
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