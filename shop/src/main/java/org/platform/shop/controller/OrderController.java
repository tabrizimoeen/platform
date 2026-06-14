package org.platform.shop.controller;


import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.order.CreateOrderRequest;
import org.platform.shop.dto.order.OrderResponse;
import org.platform.shop.entity.Order;
import org.platform.shop.enums.OrderStatus;
import org.platform.shop.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public Page<OrderResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable Long id) {
        return orderService.getById(id);
    }


    @PutMapping("/{id}/status")
    public void changeStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        orderService.changeStatus(id, status);
    }

}