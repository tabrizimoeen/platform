package org.platform.repair.dto;

import lombok.Data;

@Data
public class RegisterShopRequest {

    private String shopName;

    private String ownerName;

    private String phone;

    private String username;

    private String password;
}