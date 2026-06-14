package org.platform.shop.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long id;

    private String status;

    private BigDecimal totalPrice;

    private String createdAt;

    private String userMobile;

    private List<OrderItemResponse> items;
}