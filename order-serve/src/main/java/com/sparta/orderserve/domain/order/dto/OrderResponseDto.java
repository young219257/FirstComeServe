package com.sparta.orderserve.domain.order.dto;

import com.sparta.orderserve.domain.order.entity.Order;
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


    public static OrderResponseDto from(Order order,List<OrderItemResponseDto> orderItems) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderItems(orderItems).build();
    }

}
