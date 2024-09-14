package com.sparta.orderserve.domain.order.dto;


import com.sparta.orderserve.domain.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StockUpdateDto {

    private Long productId;
    private int quantity;


    public static StockUpdateDto from(OrderItem orderItem){

        return StockUpdateDto.
                builder().
                productId(orderItem.getProductId()).
                quantity(orderItem.getQuantity()).build();
    }

}
