package com.sparta.productserve.domain.wishlist.repository;

import com.sparta.productserve.domain.wishlist.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Long> {
    WishList findByUserId(Long userId);
}
