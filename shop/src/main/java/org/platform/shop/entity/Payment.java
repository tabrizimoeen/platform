package org.platform.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments", schema = "shops")
public class Payment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(unique = true)
    private String authority;

    @Column(unique = true)
    private String refId;

    private String status; // INIT, PENDING, PAID, FAILED

    private LocalDateTime paidAt;
}