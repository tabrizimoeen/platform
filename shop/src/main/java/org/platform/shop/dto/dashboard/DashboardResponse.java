package org.platform.shop.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardResponse {

    private long totalUsers;

    private long totalProducts;

    private long totalOrders;

    private BigDecimal totalSales;
}