package org.platform.shop.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private Long addressId;

    private List<OrderItemRequest> items;
}