package com.sparta.userserve.domain.delivery.entity;


import com.sparta.userserve.domain.delivery.type.DeliveryStatus;
import com.sparta.userserve.domain.order.dto.OrderRequestDto;
import com.sparta.userserve.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

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





    public static Delivery of(Order order, OrderRequestDto orderRequestDto) {
        return Delivery.builder().
                order(order).
                address(orderRequestDto.getAddress()).
                phoneNumber(orderRequestDto.getPhoneNumber()).
                deliveryStatus(DeliveryStatus.READY_DELIVERY).
                build();
    }



}
