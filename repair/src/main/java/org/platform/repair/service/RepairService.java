package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.entity.Invoice;
import org.platform.repair.entity.RepairLog;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.enums.RepairStatus;
import org.platform.repair.repository.InvoiceRepository;
import org.platform.repair.repository.RepairLogRepository;
import org.platform.repair.repository.RepairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairRepository repairRepository;
    private final RepairLogRepository logRepository;
    private final InvoiceRepository invoiceRepository;

    public RepairOrder create(RepairOrder order) {

        order.setStatus(RepairStatus.RECEIVED);
        RepairOrder saved = repairRepository.save(order);

        addLog(saved.getId(), "Device received", "RECEIVED");

        return saved;
    }

    public Page<RepairOrder> findAll(Pageable page) {
        return repairRepository.findAll(page);
    }

    public RepairOrder updateStatus(Long id, RepairStatus status) {

        RepairOrder order = repairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        order.setStatus(status);
        repairRepository.save(order);

        addLog(id, "Status changed to " + status, status.name());

        return order;
    }

    private void addLog(Long repairId, String message, String status) {
        logRepository.save(
                RepairLog.builder()
                        .repairId(repairId)
                        .message(message)
                        .status(status)
                        .build()
        );
    }

    public RepairOrder findById(Long id) {
        return repairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Invoice markPaid(Long repairId) {

        Invoice invoice =
                invoiceRepository
                        .findByRepairOrderId(repairId)
                        .orElseThrow();

        invoice.setPaid(true);

        return invoiceRepository.save(invoice);
    }
}
