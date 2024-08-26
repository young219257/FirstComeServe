package com.sparta.userserve.domain.product.dto;


import com.sparta.userserve.domain.product.entity.Product;
import com.sparta.userserve.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponseDto {
    private Long productId;
    private String productName;
    private Long productPrice;


    public static ProductListResponseDto from(Product product) {
        return ProductListResponseDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getPrice())
                .build();
    }

}
