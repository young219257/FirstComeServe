package com.sparta.firstseversystem.product.entity;

import com.sparta.firstseversystem.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;


}
