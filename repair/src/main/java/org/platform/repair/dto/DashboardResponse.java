package org.platform.repair.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardResponse {

    private long totalRepairs;
    private long totalCustomers;
    private long todayRepairs;

    private long pendingRepairs;
    private long waitingPartsRepairs;

    private long inProgressRepairs;
    private long readyRepairs;

    private long deliveredRepairs;

    private BigDecimal todayRevenue;
    private BigDecimal totalRevenue;

    private BigDecimal unpaidRevenue;

    private Map<String, Long> statusCount;
}