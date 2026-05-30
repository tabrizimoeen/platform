package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CreateRepairRequest;
import org.platform.repair.dto.TrackingResponse;
import org.platform.repair.entity.Customer;
import org.platform.repair.entity.RepairLog;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.entity.RepairShop;
import org.platform.repair.enums.RepairStatus;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.RepairLogRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.repository.RepairShopRepository;
import org.platform.repair.security.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairRepository repairRepository;
    private final RepairLogRepository logRepository;
    private final RepairShopRepository repairShopRepository;
    private final CustomerRepository customerRepository;

    public RepairOrder create(CreateRepairRequest req) {

        Long shopId = TenantContext.get();

        RepairShop shop = repairShopRepository.findById(shopId).orElseThrow(() -> new RuntimeException("Shop not found"));

        Customer customer = customerRepository.findByNameAndShopId(req.getCustomerName(), shopId).orElseGet(() -> {

            Customer c = new Customer();

            c.setName(req.getCustomerName());
            c.setPhone(req.getPhone());
            c.setShop(shop);

            return customerRepository.save(c);
        });

        RepairOrder repair = new RepairOrder();

        repair.setCustomer(customer);
        repair.setShop(shop);

        repair.setDeviceModel(req.getDeviceModel());
        repair.setProblemDescription(req.getProblemDescription());

        repair.setEstimatedCost(req.getEstimatedCost());
        repair.setImei(req.getImei());
        repair.setStatus(RepairStatus.RECEIVED);
        if (req.getImei() != null && !req.getImei().isBlank() && repairRepository.existsByShopIdAndImei(shopId, req.getImei())) {
            throw new RuntimeException("IMEI already exists");
        }
        RepairOrder saved = repairRepository.save(repair);

        addLog(saved.getId(), "Device received", "RECEIVED");

        return saved;
    }

    public Page<RepairOrder> findAll(Pageable page) {

        Long shopId = TenantContext.get();

        return repairRepository.findByShopIdOrderByIdDesc(shopId, page);
    }

    public RepairOrder updateStatus(Long id, RepairStatus status) {
        Long shopId = TenantContext.get();

        RepairOrder order = repairRepository.findByIdAndShopId(id, shopId).orElseThrow(() -> new RuntimeException("Repair not found"));
        if (order.getStatus() == status) {
            return order;
        }
        if (!RepairStatusValidator.canMove(order.getStatus(), status)) {
            throw new IllegalStateException("Invalid status transition: " + order.getStatus() + " -> " + status);
        }


        order.setStatus(status);
        RepairOrder saved = repairRepository.save(order);

        addLog(id, "وضعیت به " + status.getLabelFa() +" تغییر کرد", status.name());

        return saved;
    }

    private void addLog(Long repairId, String message, String status) {
        Long shopId = TenantContext.get();

        RepairShop shop = repairShopRepository.findById(shopId).orElseThrow();

        logRepository.save(RepairLog.builder().repairId(repairId).message(message).status(status).shop(shop).build());
    }

    public RepairOrder findById(Long id) {
        Long shopId = TenantContext.get();

        return repairRepository.findByIdAndShopId(id, shopId).orElseThrow(() -> new RuntimeException("Repair not found"));
    }

    public TrackingResponse track(Long repairId) {
        Long shopId = TenantContext.get();
        RepairOrder repair = repairRepository.findByIdAndShopId(repairId, shopId).orElseThrow(() -> new RuntimeException("Repair not found"));

        return new TrackingResponse(repair.getId(), repair.getCustomer().getName(), repair.getDeviceModel(), repair.getStatus().name(), repair.getEstimatedCost());

    }

    public RepairOrder createOrGetCustomerAndCreateRepair(String customerName, String phone, String deviceModel, String problemDescription, BigDecimal estimatedCost,String imei) {

        Long shopId = TenantContext.get();

        Customer customer = customerRepository.findByNameAndShopId(customerName, shopId).orElseGet(() -> {
            Customer c = new Customer();
            c.setName(customerName);
            c.setPhone(phone);
            c.setShop(repairShopRepository.findById(shopId).orElseThrow());
            return customerRepository.save(c);
        });

        RepairOrder repair = new RepairOrder();
        repair.setCustomer(customer);
        repair.setShop(customer.getShop());
        repair.setDeviceModel(deviceModel);
        repair.setImei(imei);
        repair.setProblemDescription(problemDescription);
        repair.setEstimatedCost(estimatedCost);
        repair.setStatus(RepairStatus.RECEIVED);

        RepairOrder saved = repairRepository.save(repair);

        addLog(saved.getId(), "Repair created", "RECEIVED");

        return saved;
    }

    public RepairOrder findByImei(String imei) {

        Long shopId = TenantContext.get();

        return repairRepository.findByImeiAndShopId(imei, shopId).orElseThrow(() -> new RuntimeException("Repair not found"));
    }

    public List<RepairOrder> searchByImei(String imei) {

        Long shopId = TenantContext.get();

        return repairRepository.findTop10ByShopIdAndImeiContainingIgnoreCaseOrderByIdDesc(shopId, imei);
    }
}
