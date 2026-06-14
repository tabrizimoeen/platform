package org.platform.shop.controller;

import lombok.RequiredArgsConstructor;
import org.platform.shop.entity.Order;
import org.platform.shop.service.CheckoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public Order checkout(@RequestParam Long addressId) {
        return checkoutService.checkout(addressId);
    }
}