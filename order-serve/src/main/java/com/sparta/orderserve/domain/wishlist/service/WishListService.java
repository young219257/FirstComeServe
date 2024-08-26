package com.sparta.orderserve.domain.wishlist.service;


import com.sparta.orderserve.domain.product.dto.ProductResponseDto;
import com.sparta.orderserve.domain.user.entity.User;
import com.sparta.orderserve.domain.wishlist.dto.WishListRequestDto;
import com.sparta.orderserve.domain.wishlist.dto.WishListResponseDto;
import org.springframework.data.domain.Page;

public interface WishListService {
    void addProductToWishlist(User user, WishListRequestDto wishListRequestDto);

    Page<WishListResponseDto> getWishlist(User user, int page, int size, String sortBy, boolean isAsc);

    void updateWishListItemQuantity(User user, Long wishListItemId, WishListRequestDto wishListUpdateRequestDto);

    void deleteWishListItem(User user, Long wishListItemId);

    ProductResponseDto getWishlistItem(User user, Long wishListItemId);
}
