package com.sparta.firstseversystem.domain.order.dto;

import com.sparta.firstseversystem.domain.order.entity.Order;
import com.sparta.firstseversystem.domain.order.entity.OrderItem;
import com.sparta.firstseversystem.domain.order.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;
    private List<OrderItemResponseDto> orderItems;


    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder().
                orderId(order.getId()).
                orderItems(order.getOrderItems().stream().map(OrderItemResponseDto::from).toList()).
                build();
    }

}
