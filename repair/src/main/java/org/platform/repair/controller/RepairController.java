package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CreateRepairRequest;
import org.platform.repair.dto.TrackingResponse;
import org.platform.repair.entity.RepairLog;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.enums.RepairStatus;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.RepairLogRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.repository.RepairShopRepository;
import org.platform.repair.security.TenantContext;
import org.platform.repair.service.RepairService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repairs")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;
    private final RepairRepository repairRepository;
    private final RepairLogRepository logRepository;

    // -------------------------
    // CREATE (TENANT SAFE)
    // -------------------------
    @PostMapping
    public RepairOrder create(@RequestBody CreateRepairRequest req) {

        return repairService.create(req);
    }

    // -------------------------
    // LIST (TENANT SAFE)
    // -------------------------
    @GetMapping
    public Page<RepairOrder> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return repairRepository.findByShopId(
                TenantContext.get(),
                PageRequest.of(page, size)
        );
    }

    // -------------------------
    // GET BY ID (TENANT SAFE)
    // -------------------------
    @GetMapping("/{id}")
    public RepairOrder findById(@PathVariable Long id) {

        Long shopId = TenantContext.get();

        return repairRepository
                .findByIdAndShopId(id, shopId)
                .orElseThrow(
                        () -> new RuntimeException("Not found")
                );
    }

    // -------------------------
    // TRACK (PUBLIC BUT SAFE)
    // -------------------------
    @GetMapping("/track/{id}")
    public TrackingResponse track(@PathVariable Long id) {

        return repairService.track(id);
    }

    // -------------------------
    // LOGS (TENANT SAFE)
    // -------------------------
    @GetMapping("/{id}/logs")
    public List<RepairLog> getLogs(@PathVariable Long id) {

        Long shopId = TenantContext.get();

        return logRepository.findByRepairIdAndShopIdOrderByCreatedAtAsc(id, shopId);
    }

    // -------------------------
    // STATUS UPDATE
    // -------------------------
    @PatchMapping("/{id}/status")
    public RepairOrder updateStatus(
            @PathVariable Long id,
            @RequestParam RepairStatus status
    ) {
        return repairService.updateStatus(id, status);
    }

    // -------------------------
    // SEARCH (FIX REQUIRED TOO)
    // -------------------------
    @GetMapping("/search")
    public List<RepairOrder> search(@RequestParam String query) {
        return repairRepository.search(TenantContext.get(), query);
    }

    // -------------------------
    // STATUS FILTER
    // -------------------------
    @GetMapping("/status")
    public List<RepairOrder> byStatus(@RequestParam RepairStatus status) {
        return repairRepository.findByShopIdAndStatus(
                TenantContext.get(),
                status
        );
    }

    @PostMapping("/smart-create")
    public RepairOrder smartCreate(@RequestBody CreateRepairRequest req) {

        return repairService.createOrGetCustomerAndCreateRepair(
                req.getCustomerName(),
                req.getPhone(),
                req.getDeviceModel(),
                req.getProblemDescription(),
                req.getEstimatedCost(),
                req.getImei()
        );
    }

    @GetMapping("/{id}/repairs")
    public List<RepairOrder> getRepairs(@PathVariable(name = "id") Long customerId) {
        return repairRepository.findByCustomerIdAndShopId(
                customerId,
                TenantContext.get()
        );
    }
    @GetMapping("/imei/{imei}")
    public RepairOrder findByImei(
            @PathVariable String imei
    ) {
        return repairService.findByImei(imei);
    }
    @GetMapping("/imei-search")
    public List<RepairOrder> imeiSearch(
            @RequestParam String q
    ) {
        return repairService.searchByImei(q);
    }
}