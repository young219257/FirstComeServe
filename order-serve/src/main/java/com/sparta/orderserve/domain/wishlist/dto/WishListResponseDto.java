package com.sparta.orderserve.domain.wishlist.dto;

import com.sparta.orderserve.domain.wishlist.entity.WishListItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListResponseDto {
    private Long wishlistId;
    private String productName;
    private int quantity;
    private Long price;

    public static WishListResponseDto from(WishListItem wishListItem) {
        return WishListResponseDto.builder().
                wishlistId(wishListItem.getId()).
                productName(wishListItem.getProduct().getProductName()).
                quantity(wishListItem.getQuantity()).
                price(wishListItem.getProduct().getPrice()).build();
    }
}
