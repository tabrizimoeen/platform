package org.platform.shop.controller;

import lombok.RequiredArgsConstructor;
import org.platform.shop.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public String start(@PathVariable Long orderId) {
        return paymentService.startPayment(orderId);
    }

    @GetMapping("/callback")
    public String callback(
            @RequestParam String Authority,
            @RequestParam String Status,
            @RequestParam Long orderId
    ) {
        return paymentService.verifyPayment(Authority, Status, orderId);
    }
}