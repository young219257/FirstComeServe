package com.sparta.productserve.domain.order.dto;

import com.sparta.productserve.domain.order.entity.Order;
import com.sparta.productserve.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
