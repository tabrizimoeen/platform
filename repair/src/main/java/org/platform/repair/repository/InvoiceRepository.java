package org.platform.repair.repository;

import org.platform.repair.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByRepairOrderId(Long repairId);
}