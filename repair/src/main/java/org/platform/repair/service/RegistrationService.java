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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RepairShopRepository shopRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterShopRequest req) {

        if (req.getShopName() == null || req.getShopName().isBlank()) {
            throw new RuntimeException("نام تعمیرگاه الزامی است");
        }
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new RuntimeException("نام کاربری الزامی است");
        }
        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new RuntimeException("رمز عبور باید حداقل ۶ کاراکتر باشد");
        }
        if (userRepository.existsByUsername(req.getUsername().trim())) {
            throw new RuntimeException("این نام کاربری قبلاً ثبت شده است");
        }

        RepairShop shop =
                RepairShop.builder()
                        .name(req.getShopName().trim())
                        .ownerName(req.getOwnerName())
                        .phone(req.getPhone())
                        .createdAt(LocalDateTime.now())
                        .build();
        shop = shopRepository.save(shop);

        User user =
                User.builder()
                        .username(req.getUsername().trim())
                        .password(
                                passwordEncoder.encode(
                                        req.getPassword()
                                )
                        )
                        .role(UserRole.OWNER)
                        .active(true)
                        .shop(shop)
                        .build();

        userRepository.save(user);
    }
}