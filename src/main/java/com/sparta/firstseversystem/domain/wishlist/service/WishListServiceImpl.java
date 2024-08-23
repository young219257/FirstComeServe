package com.sparta.firstseversystem.domain.wishlist.service;

import com.sparta.firstseversystem.domain.product.entity.Product;
import com.sparta.firstseversystem.domain.product.repository.ProductRepository;
import com.sparta.firstseversystem.domain.user.entity.User;
import com.sparta.firstseversystem.domain.wishlist.dto.WishListRequestDto;
import com.sparta.firstseversystem.domain.wishlist.dto.WishListResponseDto;
import com.sparta.firstseversystem.domain.wishlist.entity.WishList;
import com.sparta.firstseversystem.domain.wishlist.entity.WishListItem;
import com.sparta.firstseversystem.domain.wishlist.repository.WishListItemRepository;
import com.sparta.firstseversystem.domain.wishlist.repository.WishListRepository;
import com.sparta.firstseversystem.global.exception.ErrorCode;
import com.sparta.firstseversystem.global.exception.NotfoundResourceException;
import com.sparta.firstseversystem.global.exception.UnAuthorizedAccessException;
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

    /**
     * 상품을 wishlist에 추가하는 메소드
     * @Param wishlistId
     * @RequestDto {productId, quantity}
     * return 성공 여부
     **/
    @Override
    public void addProductToWishlist(User user, Long wishlistId, WishListRequestDto wishListRequestDto) {
        WishList wishList=findWishlist(wishlistId);

        //wishList의 주인장(?)이 현재 접근하려는 user와 다를 경우
        if(!wishList.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException(ErrorCode.NOT_ACCESS_WISHLIST);
        }
        Product product=productRepository.findById(wishListRequestDto.getProductId()).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));
        WishListItem wishListItem=WishListItem.builder().
                wishList(wishList).
                product(product).
                quantity(wishListRequestDto.
                getQuantity()).
                build();
        wishListItemRepository.save(wishListItem); //위시리스트에 상품 담기
    }

    @Override
    public Page<WishListResponseDto> getWishlist(User user, Long wishlistId, int page, int size, String sortBy, boolean isAsc) {

        WishList wishlist=findWishlist(wishlistId);

        //wishList의 주인장(?)이 현재 접근하려는 user와 다를 경우
        if(!wishlist.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException(ErrorCode.NOT_ACCESS_WISHLIST);
        }

        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<WishListItem> wishlists = wishListItemRepository.findALlByWishListId(wishlistId,pageable);
        return wishlists.map(wishList -> new WishListResponseDto());
    }

    @Override
    public void updateWishListItemQuantity(User user, Long wishListItemId, WishListRequestDto wishListUpdateRequestDto) {
        WishListItem wishListItem=findWishlistItem(wishListItemId);
        wishListItem.builder().quantity(wishListUpdateRequestDto.getQuantity()).build();
        wishListItemRepository.save(wishListItem);
    }

    @Override
    public void deleteWishListItem(User user, Long wishListItemId) {
        wishListItemRepository.delete(findWishlistItem(wishListItemId));
    }


    //위시리스트를 찾는 메소드
    private WishList findWishlist(Long wishlistId) {
        return wishlistRepository.findById(wishlistId).orElseThrow(()-> new NotfoundResourceException(ErrorCode.NOTFOUND_WISHLIST));
    }
    //위시리스트 아이템을 찾는 메소드
    private WishListItem findWishlistItem(Long wishListItemId) {

        return wishListItemRepository.findById(wishListItemId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_WISHLISTITEM));
    }
}
