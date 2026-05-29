package org.platform.repair.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateRepairRequest {

    private Long repairId;
    private String customerName;
    private String phone;
    private String imei;
    private String deviceModel;
    private String status;
    private String problemDescription;
    private BigDecimal estimatedCost;
    private List<RepairLog> logs;
}