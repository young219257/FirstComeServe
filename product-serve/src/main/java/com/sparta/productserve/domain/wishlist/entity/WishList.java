package com.sparta.productserve.domain.wishlist.entity;


import com.sparta.productserve.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="wishlists")
public class WishList extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id",nullable = false)
//    private User user;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "wishList",cascade = CascadeType.REMOVE)
    private List<WishListItem> wishListItems;



}
