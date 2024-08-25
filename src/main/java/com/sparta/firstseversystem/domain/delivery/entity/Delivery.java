package com.sparta.firstseversystem.domain.delivery.entity;


import com.sparta.firstseversystem.domain.delivery.type.DeliveryStatus;
import com.sparta.firstseversystem.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private DeliveryStatus deliveryStatus;

    @Column
    @Setter
    private LocalDateTime startedAt;

    @Column
    @Setter
    private LocalDateTime completedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",nullable = false)
    private Order order;


}
