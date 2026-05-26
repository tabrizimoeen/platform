package org.platform.repair.repository;

import org.platform.repair.entity.RepairLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairLogRepository extends JpaRepository<RepairLog, Long> {

    List<RepairLog> findByRepairIdOrderByCreatedAtAsc(Long repairId);
}