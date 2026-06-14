package org.platform.shop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.order.CreateOrderRequest;
import org.platform.shop.dto.order.OrderItemRequest;
import org.platform.shop.entity.Cart;
import org.platform.shop.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartService cartService;
    private final OrderService orderService;

    @Transactional
    public Order checkout(Long addressId) {

        Cart cart = cartService.getCart();

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        CreateOrderRequest req = new CreateOrderRequest();

        req.setAddressId(addressId);

        List<OrderItemRequest> items =
                cart.getItems().stream()
                        .map(i -> {
                            OrderItemRequest r = new OrderItemRequest();
                            r.setProductId(i.getProduct().getId());
                            r.setQuantity(i.getQuantity());
                            return r;
                        })
                        .toList();

        req.setItems(items);

        Order order = orderService.createOrder(req);

        cart.getItems().clear();

        return order;
    }
}