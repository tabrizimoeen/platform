package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.LoginRequest;
import org.platform.repair.entity.User;
import org.platform.repair.repository.UserRepository;
import org.platform.repair.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody LoginRequest request
    ) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (!user.getPassword()
                .equals(request.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        String token =
                jwtUtil.generate(user.getUsername());

        return Map.of("token", token);
    }
}
