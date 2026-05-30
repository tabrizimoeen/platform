package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.RegisterShopRequest;
import org.platform.repair.entity.RepairShop;
import org.platform.repair.entity.User;
import org.platform.repair.enums.UserRole;
import org.platform.repair.repository.RepairShopRepository;
import org.platform.repair.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RepairShopRepository shopRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterShopRequest req) {

        if (userRepository.existsByUsername(
                req.getUsername()
        )) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        RepairShop shop =
                RepairShop.builder()
                        .name(req.getShopName())
                        .ownerName(req.getOwnerName())
                        .phone(req.getPhone())
                        .build();
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }

        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        shop = shopRepository.save(shop);

        User user =
                User.builder()
                        .username(req.getUsername())
                        .password(
                                passwordEncoder.encode(
                                        req.getPassword()
                                )
                        )
                        .role(UserRole.OWNER)
                        .shop(shop)
                        .build();

        userRepository.save(user);
    }
}