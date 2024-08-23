package com.sparta.firstseversystem.domain.product.dto;


import com.sparta.firstseversystem.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private Long productId;
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productQuantity;

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .productQuantity(product.getStockQuantity())
                .build();
    }
}
