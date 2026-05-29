package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.LoginRequest;
import org.platform.repair.dto.LoginResponse;
import org.platform.repair.dto.RegisterShopRequest;
import org.platform.repair.entity.User;
import org.platform.repair.repository.UserRepository;
import org.platform.repair.security.AuthService;
import org.platform.repair.security.JwtService;
import org.platform.repair.service.RegistrationService;
import org.platform.repair.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegistrationService registrationService;


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public void register(
            @RequestBody RegisterShopRequest request
    ) {
        registrationService.register(request);
    }
}
