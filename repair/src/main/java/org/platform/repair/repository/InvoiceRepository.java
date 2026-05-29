package org.platform.repair.repository;

import org.platform.repair.entity.Invoice;
import org.platform.repair.entity.RepairShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    @Query("""
            SELECT COALESCE(SUM(i.amount), 0)
            FROM Invoice i
            WHERE i.shop.id = :shopId AND i.paid = true
            """)
    BigDecimal sumPaidInvoices(Long shopId);

    @Query("""
                SELECT COALESCE(SUM(i.amount), 0)
                FROM Invoice i
                WHERE i.shop.id = :shopId
                AND i.repairOrder.customer.id = :customerId
                AND i.paid = true
            """)
    BigDecimal sumByCustomer(
            Long shopId,
            Long customerId
    );

    Optional<Invoice> findByRepairOrderIdAndShopId(Long repairId, Long shopId);

    @Query("""
    SELECT COALESCE(SUM(i.amount), 0)
    FROM Invoice i
    WHERE i.shop.id = :shopId
    AND i.paid = false
""")
    BigDecimal sumUnpaidInvoices(Long shopId);

    @Query("""
    SELECT COALESCE(SUM(i.amount), 0)
    FROM Invoice i
    WHERE i.shop.id = :shopId
    AND i.paid = true
    AND FUNCTION('DATE', i.createdAt) = CURRENT_DATE
""")
    BigDecimal sumTodayRevenue(Long shopId);

    Optional<Invoice> findByIdAndShopId(
            Long id,
            Long shopId
    );
}