package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CreateInvoiceRequest;
import org.platform.repair.entity.Invoice;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.repository.InvoiceRepository;
import org.platform.repair.repository.RepairRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final RepairRepository repairRepository;

    public Invoice create(Long repairId, CreateInvoiceRequest req) {

        RepairOrder repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new RuntimeException("Repair not found"));

        return invoiceRepository.findByRepairOrderId(repairId)
                .orElseGet(() -> {
                    Invoice invoice = Invoice.builder()
                            .repairOrder(repair)
                            .amount(req.getAmount())
                            .paid(false)
                            .build();

                    return invoiceRepository.save(invoice);
                });
    }

    public Invoice getByRepair(Long repairId) {
        return invoiceRepository.findByRepairOrderId(repairId)
                .orElse(null);
    }

    public Invoice markPaid(Long repairId) {

        Invoice invoice = invoiceRepository.findByRepairOrderId(repairId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setPaid(true);
        return invoiceRepository.save(invoice);
    }
}