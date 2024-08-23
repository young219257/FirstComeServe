package com.sparta.firstseversystem.domain.order.entity;


import com.sparta.firstseversystem.domain.user.entity.User;
import com.sparta.firstseversystem.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;
}
