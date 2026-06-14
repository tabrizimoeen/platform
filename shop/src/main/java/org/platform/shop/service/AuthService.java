package org.platform.shop.service;


import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.auth.AuthResponse;
import org.platform.shop.dto.auth.LoginRequest;
import org.platform.shop.dto.auth.RegisterRequest;
import org.platform.shop.entity.User;
import org.platform.shop.enums.UserRole;
import org.platform.shop.repository.UserRepository;
import org.platform.shop.security.JwtService;
import org.springframework.security.crypto.password.
        PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobile(request.getMobile());

        user.setPasswordHash(
                encoder.encode(request.getPassword()));

        user.setRole(UserRole.ADMIN);

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        User user =
                userRepository.findByMobile(
                                request.getMobile())
                        .orElseThrow();

        if (!encoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new RuntimeException(
                    "Invalid credentials");
        }

        String token =
                jwtService.generateToken(
                        user.getMobile());

        return new AuthResponse(token);
    }
}