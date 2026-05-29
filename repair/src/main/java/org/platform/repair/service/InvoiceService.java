package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CreateInvoiceRequest;
import org.platform.repair.entity.Invoice;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.repository.InvoiceRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.security.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final RepairRepository repairRepository;

    public Invoice createOrUpdate(Long repairId, CreateInvoiceRequest req) {

        Long shopId = TenantContext.get();

        RepairOrder repair = repairRepository.findByIdAndShopId(repairId, shopId)
                .orElseThrow(() -> new RuntimeException("Repair not found"));

        return invoiceRepository
                .findByRepairOrderIdAndShopId(repairId, shopId)
                .map(existing -> {
                    existing.setAmount(req.getAmount());
                    return invoiceRepository.save(existing);
                })
                .orElseGet(() -> {
                    Invoice invoice = Invoice.builder()
                            .repairOrder(repair)
                            .shop(repair.getShop())
                            .amount(req.getAmount())
                            .paid(false)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return invoiceRepository.save(invoice);
                });
    }

    public Invoice getByRepair(Long repairId) {
        Long shopId = TenantContext.get();

        return invoiceRepository
                .findByRepairOrderIdAndShopId(
                        repairId,
                        shopId
                )
                .orElse(null);
    }
    public Invoice markPaid(Long repairId) {

        Long shopId = TenantContext.get();

        Invoice invoice =
                invoiceRepository
                        .findByRepairOrderIdAndShopId(
                                repairId,
                                shopId
                        )
                        .orElseThrow(
                                () -> new RuntimeException("Invoice not found")
                        );

        if(Boolean.TRUE.equals(invoice.getPaid())){
            return invoice;
        }

        invoice.setPaid(true);

        RepairOrder repair = invoice.getRepairOrder();
        repair.setFinalCost(invoice.getAmount());

        repairRepository.save(repair);

        return invoiceRepository.save(invoice);
    }
}