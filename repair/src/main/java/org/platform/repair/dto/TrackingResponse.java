package org.platform.repair.dto;

import java.math.BigDecimal;

public record TrackingResponse(
        Long repairId,
        String customerName,
        String deviceModel,
        String status,
        BigDecimal estimatedCost
) {
}
