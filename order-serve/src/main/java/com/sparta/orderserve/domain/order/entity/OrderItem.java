package com.sparta.orderserve.domain.order.entity;


import com.sparta.orderserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.orderserve.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="orderitems")
public class OrderItem extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",nullable = false)
    private Order order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="product_id",nullable = false)
//    private Product product;

    @Column(nullable = false)
    private Long productId;


    public static OrderItem of(Order order, Long productId, OrderItemRequestDto orderItemRequestDto) {
        return OrderItem.builder().
                order(order).
                productId(productId).
                quantity(orderItemRequestDto.getQuantity()).
                build();
    }




}
