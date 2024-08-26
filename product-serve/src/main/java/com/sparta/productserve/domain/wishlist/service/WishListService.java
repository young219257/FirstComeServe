package com.sparta.productserve.domain.wishlist.service;


import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.user.entity.User;
import com.sparta.productserve.domain.wishlist.dto.WishListRequestDto;
import com.sparta.productserve.domain.wishlist.dto.WishListResponseDto;
import org.springframework.data.domain.Page;

public interface WishListService {
    void addProductToWishlist(User user, WishListRequestDto wishListRequestDto);

    Page<WishListResponseDto> getWishlist(User user, int page, int size, String sortBy, boolean isAsc);

    void updateWishListItemQuantity(User user, Long wishListItemId, WishListRequestDto wishListUpdateRequestDto);

    void deleteWishListItem(User user, Long wishListItemId);

    ProductResponseDto getWishlistItem(User user, Long wishListItemId);
}
