package org.platform.shop.service;

import lombok.RequiredArgsConstructor;
import org.platform.shop.entity.Order;
import org.platform.shop.entity.Payment;
import org.platform.shop.enums.OrderStatus;
import org.platform.shop.repository.OrderRepository;
import org.platform.shop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ZarinpalService zarinpalService;

    public String startPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setStatus("PENDING");

        paymentRepository.save(payment);

        String url = zarinpalService.requestPayment(
                order.getTotalPrice().longValue(),
                orderId
        );

        return url;
    }

    public String verifyPayment(String authority, String status, Long orderId) {

        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow();

        if (!"OK".equals(status)) {

            payment.setStatus("FAILED");
            paymentRepository.save(payment);

            return "Payment failed";
        }

        payment.setStatus("PAID");
        payment.setAuthority(authority);

        paymentRepository.save(payment);

        // اینجا اتصال به OrderService
        return "Payment success";
    }
}