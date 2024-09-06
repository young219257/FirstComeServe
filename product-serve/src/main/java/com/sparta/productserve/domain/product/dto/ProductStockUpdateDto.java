package com.sparta.productserve.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockUpdateDto {

    private Long productId;
    private int quantity;
}
