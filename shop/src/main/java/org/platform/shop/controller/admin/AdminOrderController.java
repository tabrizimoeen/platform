package org.platform.shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.order.ChangeOrderStatusRequest;
import org.platform.shop.entity.Order;
import org.platform.shop.enums.OrderStatus;
import org.platform.shop.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderRepository orderRepository;

    @GetMapping
    public Object getAllOrders() {
        return orderRepository.findAll();
    }

    @PatchMapping("/{id}/status")
    public Order changeStatus(
            @PathVariable Long id,
            @RequestBody ChangeOrderStatusRequest request
    ) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow();

        order.setStatus(
                OrderStatus.valueOf(
                        request.getStatus()
                )
        );

        return orderRepository.save(order);
    }
}