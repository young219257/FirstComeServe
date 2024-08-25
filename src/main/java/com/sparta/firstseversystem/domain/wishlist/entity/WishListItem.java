package com.sparta.firstseversystem.domain.wishlist.entity;

import com.sparta.firstseversystem.domain.product.entity.Product;
import com.sparta.firstseversystem.global.entity.TimeStamped;
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


}
