package org.platform.repair.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.platform.repair.entity.RepairOrder;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomerDetailsResponse {

    private Long customerId;
    private String name;
    private String phone;

    private BigDecimal totalSpent;
    private Long totalRepairs;

    private List<RepairOrder> repairs;
}