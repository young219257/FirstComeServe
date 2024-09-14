package com.sparta.paymentserve.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDto {
    private int totalOrderPrice;
    private String orderStatus;
}
