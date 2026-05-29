package org.platform.repair.repository;

import org.platform.repair.entity.RepairOrder;
import org.platform.repair.entity.RepairShop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairShopRepository extends JpaRepository<RepairShop, Long> {

}
