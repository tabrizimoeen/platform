package org.platform.shop.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.order.CreateOrderRequest;
import org.platform.shop.dto.order.OrderItemRequest;
import org.platform.shop.dto.order.OrderItemResponse;
import org.platform.shop.dto.order.OrderResponse;
import org.platform.shop.entity.*;
import org.platform.shop.enums.OrderStatus;
import org.platform.shop.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        String mobile = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByMobile(mobile)
                .orElseThrow();

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow();

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        BigDecimal totalPrice = BigDecimal.ZERO;

        Order savedOrder = orderRepository.save(order);

        for (OrderItemRequest item : request.getItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow();

            int available = product.getInventory() - product.getReservedInventory();

            if (available < item.getQuantity()) {
                throw new RuntimeException("Inventory not enough");
            }

            BigDecimal itemPrice =
                    product.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    );

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(itemPrice);

            orderItemRepository.save(orderItem);

            // ✅ RESERVE inventory (نه کم کردن واقعی)
            product.setReservedInventory(
                    product.getReservedInventory() + item.getQuantity()
            );

            productRepository.save(product);

            totalPrice = totalPrice.add(itemPrice);
        }

        savedOrder.setTotalPrice(totalPrice);

        return orderRepository.save(savedOrder);
    }
    public Page<OrderResponse> getAll(int page, int size) {

        return orderRepository.findAll(PageRequest.of(page, size))
                .map(this::toResponse);
    }

    public OrderResponse getById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow();

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {

        OrderResponse res = new OrderResponse();

        res.setId(order.getId());
        res.setStatus(order.getStatus().name());
        res.setTotalPrice(order.getTotalPrice());
        res.setCreatedAt(order.getCreatedAt().toString());
        res.setUserMobile(order.getUser().getMobile());

        List<OrderItemResponse> items =
                orderItemRepository.findByOrderId(order.getId())
                        .stream()
                        .map(i -> {
                            OrderItemResponse r = new OrderItemResponse();
                            r.setId(i.getId());
                            r.setProductName(i.getProductName());
                            r.setUnitPrice(i.getUnitPrice());
                            r.setQuantity(i.getQuantity());
                            r.setTotalPrice(i.getTotalPrice());
                            return r;
                        })
                        .toList();

        res.setItems(items);

        return res;
    }

    @Transactional
    public void changeStatus(Long id, OrderStatus status) {

        Order order = orderRepository.findById(id)
                .orElseThrow();

        order.setStatus(status);

        orderRepository.save(order);
    }
    @Transactional
    public void cancelPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        List<OrderItem> items =
                orderItemRepository.findByOrderId(orderId);

        for (OrderItem item : items) {

            Product product = item.getProduct();

            product.setReservedInventory(
                    product.getReservedInventory() - item.getQuantity()
            );

            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }
    @Transactional
    public void confirmPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        List<OrderItem> items =
                orderItemRepository.findByOrderId(orderId);

        for (OrderItem item : items) {

            Product product = item.getProduct();

            // کم شدن واقعی موجودی
            product.setInventory(
                    product.getInventory() - item.getQuantity()
            );

            // آزاد شدن رزرو
            product.setReservedInventory(
                    product.getReservedInventory() - item.getQuantity()
            );

            productRepository.save(product);
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }
}