package org.platform.shop.controller;


import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.auth.AuthResponse;
import org.platform.shop.dto.auth.LoginRequest;
import org.platform.shop.dto.auth.RegisterRequest;
import org.platform.shop.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(
            @RequestBody RegisterRequest request) {

        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}