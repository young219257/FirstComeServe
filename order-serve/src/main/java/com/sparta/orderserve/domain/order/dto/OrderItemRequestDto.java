package com.sparta.orderserve.domain.order.dto;

import com.sparta.orderserve.domain.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDto {

    private Long productId;
    private int quantity;

    public static OrderItemRequestDto from(OrderItem orderItem) {
        return OrderItemRequestDto.builder().productId(orderItem.getProductId()).quantity(orderItem.getQuantity()).build();
    }
}
