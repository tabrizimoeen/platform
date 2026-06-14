package org.platform.shop.service;

import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.dashboard.DashboardResponse;
import org.platform.shop.entity.Order;
import org.platform.shop.repository.OrderRepository;
import org.platform.shop.repository.ProductRepository;
import org.platform.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardResponse getDashboard() {

        BigDecimal totalSales =
                orderRepository.findAll()
                        .stream()
                        .map(Order::getTotalPrice)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return DashboardResponse.builder()
                .totalUsers(
                        userRepository.count())
                .totalProducts(
                        productRepository.count())
                .totalOrders(
                        orderRepository.count())
                .totalSales(
                        totalSales)
                .build();
    }
}