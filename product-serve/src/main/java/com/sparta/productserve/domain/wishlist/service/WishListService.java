package com.sparta.productserve.domain.wishlist.service;


import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.wishlist.dto.WishListRequestDto;
import com.sparta.productserve.domain.wishlist.dto.WishListResponseDto;
import com.sparta.productserve.domain.wishlist.dto.WishListUpdateDto;
import org.springframework.data.domain.Page;

public interface WishListService {

    void createWishList(Long userId);

    void addProductToWishlist(Long userId, WishListRequestDto wishListRequestDto);

    Page<WishListResponseDto> getWishlist(Long userId, int page, int size, String sortBy, boolean isAsc);

    void updateWishListItemQuantity(Long userId, Long wishListItemId, WishListUpdateDto wishListUpdateDto);

    void deleteWishListItem(Long userId, Long wishListItemId);

    ProductResponseDto getWishlistItem(Long userId, Long wishListItemId);
}
