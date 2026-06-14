package org.platform.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.platform.shop.enums.OrderStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
        name = "orders",
        schema = "shops",
        indexes = {
                @Index(name = "idx_order_user", columnList = "user_id")
        }
)
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalPrice;
}