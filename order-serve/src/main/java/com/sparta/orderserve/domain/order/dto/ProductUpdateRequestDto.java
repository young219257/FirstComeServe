package com.sparta.orderserve.domain.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequestDto {
    private int newStockQuantity;

    public ProductUpdateRequestDto(int newStockQuantity) {
        this.newStockQuantity = newStockQuantity;
    }
}
