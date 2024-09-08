package com.sparta.productserve.domain.product.entity;
import com.sparta.productserve.global.entity.TimeStamped;
import com.sparta.productserve.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;


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
    private Long price;

    @Column(nullable = false)
    private int stockQuantity;

    public void updateStock(int newStock){
        this.stockQuantity = newStock;
    }



}
