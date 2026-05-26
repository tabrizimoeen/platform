package org.platform.repair.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateInvoiceRequest {

    private BigDecimal amount;
}