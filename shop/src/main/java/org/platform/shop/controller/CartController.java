package org.platform.shop.controller;

import lombok.RequiredArgsConstructor;
import org.platform.shop.entity.Cart;
import org.platform.shop.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public void add(@RequestParam Long productId,
                    @RequestParam Integer qty) {
        cartService.addToCart(productId, qty);
    }

    @GetMapping
    public Cart getCart() {
        return cartService.getCart();
    }

    @DeleteMapping("/remove/{productId}")
    public void remove(@PathVariable Long productId) {
        cartService.remove(productId);
    }
}