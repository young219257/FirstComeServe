package com.sparta.userserve.domain.wishlist.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WishListRequestDto {

    private Long productId;
    private int quantity;
}
