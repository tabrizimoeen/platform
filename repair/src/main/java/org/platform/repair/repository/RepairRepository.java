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

public interface RepairRepository extends JpaRepository<RepairOrder, Long> {
    @Query("""
            SELECT COUNT(r)
            FROM RepairOrder r
            WHERE r.createdAt >= :start
            AND r.createdAt < :end
            """)
    long countToday(LocalDateTime start, LocalDateTime end);

    long countByStatus(RepairStatus status);

    @Query("""
            SELECT r FROM RepairOrder r
            LEFT JOIN r.customer c
            WHERE
            LOWER(r.deviceModel) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(r.imei) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<RepairOrder> search(@Param("query") String query);
    List<RepairOrder> findByStatus(
            RepairStatus status
    );

    Page<RepairOrder> findAllByOrderByIdDesc(
            Pageable pageable
    );
}
