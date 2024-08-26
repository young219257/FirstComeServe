package com.sparta.productserve.domain.wishlist.entity;

import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.global.entity.TimeStamped;
import com.sparta.productserve.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="wishlist_items")
public class WishListItem extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wishlist_id", nullable = false)
    private WishList wishList;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id",nullable = false)
    private Product product;

    public static WishListItem of(WishList wishList, Product product, int quantity) {
        return WishListItem.builder()
                .wishList(wishList)
                .product(product)
                .quantity(quantity)
                .build();
    }


}
