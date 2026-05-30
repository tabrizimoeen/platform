package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;
import org.platform.repair.enums.UserRole;

@Entity
@Table(name = "users", schema = "repairs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private RepairShop shop;
}