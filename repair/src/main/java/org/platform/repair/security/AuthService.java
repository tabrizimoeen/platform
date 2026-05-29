package org.platform.repair.security;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.LoginRequest;
import org.platform.repair.dto.LoginResponse;
import org.platform.repair.entity.User;
import org.platform.repair.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Invalid password");
        }

        return new LoginResponse(
                jwtService.generateToken(user)
        );
    }
}