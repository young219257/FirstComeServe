package com.sparta.firstseversystem.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long productId;
    private int quantity;
    private String address;
    private String phoneNumber;
}
