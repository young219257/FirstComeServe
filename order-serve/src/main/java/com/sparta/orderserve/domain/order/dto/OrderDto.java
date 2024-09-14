package com.sparta.orderserve.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderDto {
    private long totalOrderPrice;
    private String orderStatus;
}
