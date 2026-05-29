package org.platform.repair.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers", schema = "repairs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private RepairShop shop;
    private LocalDateTime createdAt = LocalDateTime.now();
}