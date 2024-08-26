package com.sparta.userserve.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    List<OrderItemRequestDto> orderItems;
    private String address;
    private String phoneNumber;
}
