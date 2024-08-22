package com.sparta.firstseversystem.product.dto;


import com.sparta.firstseversystem.product.entity.Product;
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
