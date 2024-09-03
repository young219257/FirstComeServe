package com.sparta.productserve.domain.wishlist.service;

import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.domain.product.repository.ProductRepository;
import com.sparta.productserve.domain.product.service.ProductService;
import com.sparta.productserve.domain.wishlist.dto.WishListRequestDto;
import com.sparta.productserve.domain.wishlist.dto.WishListResponseDto;
import com.sparta.productserve.domain.wishlist.entity.WishList;
import com.sparta.productserve.domain.wishlist.entity.WishListItem;
import com.sparta.productserve.domain.wishlist.repository.WishListItemRepository;
import com.sparta.productserve.domain.wishlist.repository.WishListRepository;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final WishListItemRepository wishListItemRepository;
    private final ProductService productService;

    /**위시리스트 품목 추가 메소드 **/
    @Override
    public void addProductToWishlist(Long userId,WishListRequestDto wishListRequestDto) {
        //상품
        Product product=findProduct(wishListRequestDto.getProductId());

        //user의 위시리스트
        WishList wishList=wishlistRepository.findByUserId(userId);

        if(wishList==null){
            //wishlist 생성
            wishList = WishList.builder()
                    .userId(userId)
                    .build();
            wishlistRepository.save(wishList);
        }

        //위시리스트에 wishlistItem 생성
        WishListItem wishListItem=WishListItem.of(wishList,product,wishListRequestDto.getQuantity());

        wishListItemRepository.save(wishListItem); //위시리스트에 상품 담기
    }

    /**위시리스트 목록 조회 메소드**/
    @Override
    @Transactional
    public Page<WishListResponseDto> getWishlist(Long userId, int page, int size, String sortBy, boolean isAsc) {

        WishList wishList=findWishlist(userId);

        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<WishListItem> wishlists = wishListItemRepository.findAllByWishListId(wishList.getId(),pageable);
        return wishlists.map(WishListResponseDto::from);
    }

    /**위시리스트 품목 상세 조회 메소드**/
    @Override
    @Transactional
    public ProductResponseDto getWishlistItem(Long userId, Long wishListItemId) {
        WishListItem wishListItem=findWishlistItem(wishListItemId);
        return productService.getProduct(wishListItem.getProduct().getId());
    }

    /**위시리스트 품목 수량 변경 메소드 **/
    @Override
    @Transactional
    public void updateWishListItemQuantity(Long userId, Long wishListItemId, WishListRequestDto wishListUpdateRequestDto) {
        WishListItem wishListItem=findWishlistItem(wishListItemId);
        wishListItem.setQuantity(wishListUpdateRequestDto.getQuantity());
        wishListItemRepository.save(wishListItem);
    }

    /**위시리스트 품목 삭제 메소드 **/
    @Override
    @Transactional
    public void deleteWishListItem(Long userId, Long wishListItemId) {
        wishListItemRepository.delete(findWishlistItem(wishListItemId));
    }


    //상품 찾는 메소드
    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));

    }

    //위시리스트를 찾는 메소드
    private WishList findWishlist(Long wishListId) {
        return wishlistRepository.findById(wishListId).orElseThrow(()-> new NotfoundResourceException(ErrorCode.NOTFOUND_WISHLIST));
    }

    //위시리스트 아이템을 찾는 메소드
    private WishListItem findWishlistItem(Long wishListItemId) {

        return wishListItemRepository.findById(wishListItemId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_WISHLISTITEM));
    }
}
