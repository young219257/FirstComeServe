package com.sparta.userserve.domain.order.entity;


import com.sparta.userserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.userserve.domain.product.entity.Product;
import com.sparta.userserve.global.entity.TimeStamped;
import com.sparta.userserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.userserve.domain.product.entity.Product;
import com.sparta.userserve.global.entity.TimeStamped;
import com.sparta.userserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.userserve.domain.product.entity.Product;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id",nullable = false)
    private Product product;


    public static OrderItem of(Order order, Product product, OrderItemRequestDto orderItemRequestDto) {
        return OrderItem.builder().
                order(order).
                product(product).
                quantity(orderItemRequestDto.getQuantity()).
                build();
    }




}
