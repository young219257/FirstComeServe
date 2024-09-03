package com.sparta.productserve.domain.wishlist.repository;

import com.sparta.productserve.domain.wishlist.entity.WishListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListItemRepository  extends JpaRepository<WishListItem, Long> {
    Page<WishListItem> findAllByWishListId(Long wishlistId,Pageable pageable);
}
