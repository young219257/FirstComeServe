package com.sparta.firstseversystem.domain.order.entity;


import com.sparta.firstseversystem.domain.delivery.entity.Delivery;
import com.sparta.firstseversystem.domain.order.type.OrderStatus;
import com.sparta.firstseversystem.domain.user.entity.User;
import com.sparta.firstseversystem.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private long totalPrice;

    @Column
    @Setter
    LocalDateTime returnSignedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order",cascade = CascadeType.REMOVE)
    private Delivery delivery;



}
