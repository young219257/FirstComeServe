package com.sparta.firstseversystem.domain.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WishListResponseDto {
    private Long productId;
    private String productName;
    private int quantity;
    private Long price;
}
