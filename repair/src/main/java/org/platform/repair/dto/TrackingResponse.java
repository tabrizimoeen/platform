package org.platform.repair.dto;

import java.math.BigDecimal;

public record TrackingResponse(
        Long repairId,
        String trackingCode,
        String customerName,
        String deviceModel,
        String status,
        String statusLabel,
        BigDecimal estimatedCost
) {
}
