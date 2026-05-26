package org.platform.repair.dto;

import lombok.Data;

@Data
public class CreateRepairRequest {

    private String customerName;
    private String deviceModel;
    private String problemDescription;
    private Long estimatedCost;
}