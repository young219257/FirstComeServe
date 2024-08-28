package com.sparta.productserve.domain.wishlist.controller;

import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.wishlist.dto.WishListRequestDto;
import com.sparta.productserve.domain.wishlist.dto.WishListResponseDto;
import com.sparta.productserve.domain.wishlist.service.WishListService;
import com.sparta.productserve.global.exception.handler.dto.ApiResponse;
import com.sparta.productserve.global.security.utils.JwtUtils;
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
    private final JwtUtils jwtUtils;


    /** 상품을 wishlist에 추가하는 메소드**/
    @PostMapping("/wishlist")
    public ApiResponse addProductToWishlist(@RequestHeader("Authorization") String token,
                                            @RequestBody WishListRequestDto wishListRequestDto) {
        Long userId=jwtUtils.getUserIdFromJwt(token);
        wishlistService.addProductToWishlist(userId,wishListRequestDto);
        return ApiResponse.ok(HttpStatus.OK.value(), "위시리스트에 상품이 추가되었습니다.");
    }


    /** wishlist의 상품 목록을 조회하는 메소드 **/
    @GetMapping("/wishlists")
    public ApiResponse<Page<WishListResponseDto>> getWishlist(@RequestHeader("Authorization") String token,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                              @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {

        Long userId=jwtUtils.getUserIdFromJwt(token);
        Page<WishListResponseDto> wishLists=wishlistService.getWishlist(userId,page,size,sortBy,isAsc);
        return ApiResponse.ok(HttpStatus.OK.value(),  "위시리스트 조회에 성공하였습니다.",wishLists);

    }


    /**wishlist의 item의 정보를 상세 조회하는 메소드**/
    @GetMapping("/wishlist/{wishListItemId}")
    public ApiResponse<ProductResponseDto> getWishListItem(@RequestHeader("Authorization") String token,
                                                           @PathVariable Long wishListItemId) {
        Long userId=jwtUtils.getUserIdFromJwt(token);
        ProductResponseDto productResponseDto= wishlistService.getWishlistItem(userId,wishListItemId);
        return ApiResponse.ok(HttpStatus.OK.value(), "위시리스트 상품 상세 조회에 성공하셨습니다.",productResponseDto);
    }


    /**wishlist 수량 수정 메소드**/
    @PutMapping("/wishlist/{wishListItemId}")
    public ApiResponse updateWishListItemQuantity(@RequestHeader("Authorization") String token,
                                                  @PathVariable("wishListItemId") Long wishListItemId,
                                                  @RequestBody WishListRequestDto wishListUpdateRequestDto){
        Long userId=jwtUtils.getUserIdFromJwt(token);
        wishlistService.updateWishListItemQuantity(userId,wishListItemId,wishListUpdateRequestDto);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품의 수량이 변경되었습니다.");
    }


    /**wishlist 삭제 메소드**/
    @DeleteMapping("/wishlist/{wishListItemId}")
    public ApiResponse deleteWishListItem(@RequestHeader("Authorization") String token,
                                          @PathVariable("wishListItemId") Long wishListItemId){
        Long userId=jwtUtils.getUserIdFromJwt(token);
        wishlistService.deleteWishListItem(userId,wishListItemId);
        return ApiResponse.ok(HttpStatus.OK.value(), "해당 상품이 위시리스트에서 삭제되었습니다.");

    }


}
