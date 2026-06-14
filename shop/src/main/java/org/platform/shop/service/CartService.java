package org.platform.shop.service;

import lombok.RequiredArgsConstructor;
import org.platform.shop.entity.Cart;
import org.platform.shop.entity.CartItem;
import org.platform.shop.entity.Product;
import org.platform.shop.entity.User;
import org.platform.shop.repository.CartRepository;
import org.platform.shop.repository.ProductRepository;
import org.platform.shop.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Cart getCart() {

        User user = getCurrentUser();

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepository.save(c);
                });
    }

    public void addToCart(Long productId, Integer qty) {

        Cart cart = getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow();

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(qty);

        cart.getItems().add(item);

        cartRepository.save(cart);
    }

    public void remove(Long productId) {

        Cart cart = getCart();

        cart.getItems().removeIf(i ->
                i.getProduct().getId().equals(productId));

        cartRepository.save(cart);
    }

    private User getCurrentUser() {

        String mobile =
                SecurityContextHolder.getContext()
                        .getAuthentication().getName();

        return userRepository.findByMobile(mobile)
                .orElseThrow();
    }
}