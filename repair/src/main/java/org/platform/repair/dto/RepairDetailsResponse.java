package org.platform.repair.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.platform.repair.entity.Customer;
import org.platform.repair.entity.Invoice;
import org.platform.repair.enums.RepairStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RepairDetailsResponse {

    private Long id;

    private String deviceModel;

    private String problemDescription;

    private BigDecimal estimatedCost;

    private RepairStatus status;

    private Customer customer;

    private Invoice invoice;
    private String imei;

    private BigDecimal finalCost;
}