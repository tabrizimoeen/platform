package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CreateInvoiceRequest;
import org.platform.repair.entity.Invoice;
import org.platform.repair.security.TenantContext;
import org.platform.repair.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repairs")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/{repairId}/invoice")
    public Invoice createOrUpdate(
            @PathVariable Long repairId,
            @RequestBody CreateInvoiceRequest req
    ) {
        return invoiceService.createOrUpdate(
                repairId,
                req
        );
    }

    @GetMapping("/{repairId}/invoice")
    public Invoice get(
            @PathVariable Long repairId
    ) {
        return invoiceService.getByRepair(
                repairId
        );
    }

    @PatchMapping("/{repairId}/invoice/pay")
    public Invoice pay(
            @PathVariable Long repairId
    ) {
        return invoiceService.markPaid(
                repairId
        );
    }
}