package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CreateRepairRequest;
import org.platform.repair.entity.Customer;
import org.platform.repair.entity.RepairLog;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.enums.RepairStatus;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.RepairLogRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.service.RepairService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.platform.repair.enums.RepairStatus.*;

@RestController
@RequestMapping("/repairs")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService service;
    private final RepairRepository repairRepository;
    private final RepairLogRepository logRepository;
    private final CustomerRepository customerRepository;

    @PostMapping
    public RepairOrder create(@RequestBody CreateRepairRequest req) {

        Customer customer = customerRepository
                .findByName(req.getCustomerName())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setName(req.getCustomerName());
                    return customerRepository.save(c);
                });

        RepairOrder repair = new RepairOrder();
        repair.setCustomer(customer);
        repair.setDeviceModel(req.getDeviceModel());
        repair.setProblemDescription(req.getProblemDescription());
        repair.setEstimatedCost(req.getEstimatedCost());
        repair.setStatus(RepairStatus.RECEIVED);

        return repairRepository.save(repair);
    }
    @GetMapping
    public Page<RepairOrder> findAll(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ) {
        return service.findAll(PageRequest.of(page,size));
    }

    @GetMapping("/track/{id}")
    public RepairOrder track(@PathVariable Long id) {

        return repairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @GetMapping("/{id}/logs")
    public List<RepairLog> getLogs(@PathVariable Long id) {
        return logRepository.findByRepairIdOrderByCreatedAtAsc(id);
    }

    @GetMapping("/{id}")
    public RepairOrder findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PatchMapping("/{id}/status")
    public RepairOrder updateStatus(
            @PathVariable Long id,
            @RequestParam RepairStatus status
    ) {
        return service.updateStatus(id, status);
    }

    @GetMapping("/search")
    public List<RepairOrder> search(
            @RequestParam String query
    ) {

        return repairRepository.search(query);
    }

    @GetMapping("/status")
    public List<RepairOrder> byStatus(
            @RequestParam RepairStatus status
    ) {

        return repairRepository.findByStatus(status);
    }


}
