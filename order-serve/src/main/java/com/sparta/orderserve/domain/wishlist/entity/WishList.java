package com.sparta.orderserve.domain.wishlist.entity;


import com.sparta.orderserve.domain.user.entity.User;
import com.sparta.orderserve.global.entity.TimeStamped;
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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "wishList",cascade = CascadeType.REMOVE)
    private List<WishListItem> wishListItems;



}
