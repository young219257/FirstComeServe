package com.sparta.productserve.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateDto {

    private Long productId;
    private int quantity;
}
