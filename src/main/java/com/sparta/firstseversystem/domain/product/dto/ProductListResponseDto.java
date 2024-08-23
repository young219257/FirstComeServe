package com.sparta.firstseversystem.domain.product.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDto {
    private Long productId;
    private String productName;
    private int productPrice;


}
