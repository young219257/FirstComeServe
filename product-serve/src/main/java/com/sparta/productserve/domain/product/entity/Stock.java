package com.sparta.productserve.domain.product.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="product_id",nullable = false)
    private Product product;

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

}
