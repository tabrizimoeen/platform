package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_shop", schema = "repairs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ownerName;

    private String phone;

    private LocalDateTime createdAt = LocalDateTime.now();
}