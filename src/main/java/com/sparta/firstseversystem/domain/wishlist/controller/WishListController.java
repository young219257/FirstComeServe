package com.sparta.firstseversystem.domain.wishlist.controller;

import com.sparta.firstseversystem.domain.product.dto.ProductResponseDto;
import com.sparta.firstseversystem.domain.user.entity.User;
import com.sparta.firstseversystem.domain.wishlist.dto.WishListRequestDto;
import com.sparta.firstseversystem.domain.wishlist.dto.WishListResponseDto;
import com.sparta.firstseversystem.domain.wishlist.entity.WishListItem;
import com.sparta.firstseversystem.domain.wishlist.service.WishListService;
import com.sparta.firstseversystem.global.exception.handler.dto.ApiResponse;
import com.sparta.firstseversystem.global.security.service.CustomUserDetails;
import com.sparta.firstseversystem.global.security.utils.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishlistService;


    /** 상품을 wishlist에 추가하는 메소드**/
    @PostMapping("/wishlist")
    public ApiResponse addProductToWishlist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody WishListRequestDto wishListRequestDto) {
        wishlistService.addProductToWishlist(userDetails.getUser(),wishListRequestDto);
        return ApiResponse.ok(HttpStatus.OK.value(), "위시리스트에 상품이 추가되었습니다.");
    }

    /** wishlist의 상품 목록을 조회하는 메소드 **/
    @GetMapping("/wishlists")
    public ApiResponse<Page<WishListResponseDto>> getWishlist(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                              @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {

        User user=customUserDetails.getUser();
        Page<WishListResponseDto> wishLists=wishlistService.getWishlist(user,page,size,sortBy,isAsc);
        return ApiResponse.ok(HttpStatus.OK.value(), EncryptionUtils.decrypt(user.getUsername()) + "님의 위시리스트 조회에 성공하였습니다.",wishLists);

    }

    /**wishlist의 item의 정보를 상세 조회하는 메소드**/
    @GetMapping("/wishlist/{wishListItemId}")
    public ApiResponse<ProductResponseDto> getWishListItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @PathVariable Long wishListItemId) {
        ProductResponseDto productResponseDto= wishlistService.getWishlistItem(customUserDetails.getUser(),wishListItemId);
        return ApiResponse.ok(HttpStatus.OK.value(), "위시리스트 상품 상세 조회에 성공하셨습니다.",productResponseDto);
    }

    /**wishlist 수량 수정 메소드**/
    @PutMapping("/wishlist/{wishListItemId}")
    public ApiResponse updateWishListItemQuantity(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @PathVariable("wishListItemId") Long wishListItemId,
                                                  @RequestBody WishListRequestDto wishListUpdateRequestDto){
        wishlistService.updateWishListItemQuantity(customUserDetails.getUser(),wishListItemId,wishListUpdateRequestDto);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품의 수량이 변경되었습니다.");
    }

    /**wishlist 삭제 메소드**/
    @DeleteMapping("/wishlist/{wishListItemId}")
    public ApiResponse deleteWishListItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @PathVariable("wishListItemId") Long wishListItemId){
        wishlistService.deleteWishListItem(customUserDetails.getUser(),wishListItemId);
        return ApiResponse.ok(HttpStatus.OK.value(), "해당 상품이 위시리스트에서 삭제되었습니다.");

    }


}
