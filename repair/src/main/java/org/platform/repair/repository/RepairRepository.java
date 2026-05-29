package org.platform.repair.repository;

import org.platform.repair.entity.RepairOrder;
import org.platform.repair.enums.RepairStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RepairRepository extends JpaRepository<RepairOrder, Long> {

    // -------------------------
    // TENANT SAFE STATS
    // -------------------------
    long countByShopId(Long shopId);

    long countByShopIdAndStatus(
            Long shopId,
            RepairStatus status
    );

    @Query("""
        SELECT COUNT(r)
        FROM RepairOrder r
        WHERE r.shop.id = :shopId
        AND r.createdAt >= :start
        AND r.createdAt < :end
    """)
    long countToday(
            @Param("shopId") Long shopId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // -------------------------
    // SAFE GET
    // -------------------------
    Optional<RepairOrder> findByIdAndShopId(
            Long id,
            Long shopId
    );

    // -------------------------
    // SEARCH
    // -------------------------
    @Query("""
        SELECT r
        FROM RepairOrder r
        LEFT JOIN r.customer c
        WHERE r.shop.id = :shopId
        AND (
            LOWER(r.deviceModel) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(r.imei) LIKE LOWER(CONCAT('%', :query, '%'))
        )
        ORDER BY r.id DESC
    """)
    List<RepairOrder> search(
            @Param("shopId") Long shopId,
            @Param("query") String query
    );

    // -------------------------
    // STATUS
    // -------------------------
    List<RepairOrder> findByShopIdAndStatus(
            Long shopId,
            RepairStatus status
    );

    // -------------------------
    // LIST
    // -------------------------
    Page<RepairOrder> findByShopIdOrderByIdDesc(
            Long shopId,
            Pageable pageable
    );

    List<RepairOrder> findByShopId(
            Long shopId
    );

    Page<RepairOrder> findByShopId(
            Long shopId,
            Pageable pageable
    );
    List<RepairOrder> findByCustomerIdAndShopIdOrderByIdDesc(
            Long customerId,
            Long shopId
    );

    List<RepairOrder> findByCustomerIdAndShopId(Long id, Long aLong);

    Optional<RepairOrder> findByImeiAndShopId(
            String imei,
            Long shopId
    );
    List<RepairOrder> findTop10ByShopIdAndImeiContainingIgnoreCaseOrderByIdDesc(
            Long shopId,
            String imei
    );
    boolean existsByShopIdAndImei(
            Long shopId,
            String imei
    );
}