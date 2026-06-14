package org.platform.shop.dto;

import lombok.Data;

@Data
public class ZarinpalRequest {
    private int amount;
    private String callbackUrl;
    private String description;
}