package com.sparta.productserve.domain.wishlist.service;


import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.wishlist.dto.WishListRequestDto;
import com.sparta.productserve.domain.wishlist.dto.WishListResponseDto;
import org.springframework.data.domain.Page;

public interface WishListService {
    void addProductToWishlist(Long userId, WishListRequestDto wishListRequestDto);

    Page<WishListResponseDto> getWishlist(Long userId, int page, int size, String sortBy, boolean isAsc);

    void updateWishListItemQuantity(Long userId, Long wishListItemId, WishListRequestDto wishListUpdateRequestDto);

    void deleteWishListItem(Long userId, Long wishListItemId);

    ProductResponseDto getWishlistItem(Long userId, Long wishListItemId);
}
