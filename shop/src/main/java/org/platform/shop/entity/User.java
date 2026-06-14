package org.platform.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.platform.shop.enums.UserRole;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        schema = "shops",
        indexes = {
                @Index(name = "idx_user_mobile", columnList = "mobile")
        }
)public class User extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String mobile;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
}