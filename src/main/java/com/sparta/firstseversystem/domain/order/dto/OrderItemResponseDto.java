package com.sparta.firstseversystem.domain.order.dto;


import com.sparta.firstseversystem.domain.delivery.type.DeliveryStatus;
import com.sparta.firstseversystem.domain.order.entity.Order;
import com.sparta.firstseversystem.domain.order.entity.OrderItem;
import com.sparta.firstseversystem.domain.order.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    private String orderItemName;
    private Long orderItemPrice;
    private int orderItemQuantity;
    private OrderStatus orderStatus; //주문상태
    private DeliveryStatus deliveryStatus;

    public static OrderItemResponseDto from(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .orderItemName(orderItem.getProduct().getProductName())
                .orderItemPrice(orderItem.getProduct().getPrice())
                .orderItemQuantity(orderItem.getQuantity())
                .orderStatus(orderItem.getOrder().getOrderStatus())
                .deliveryStatus(orderItem.getOrder().getDelivery().getDeliveryStatus())
                .build();
    }
}
