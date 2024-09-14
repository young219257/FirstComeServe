package com.sparta.productserve.domain.product.repository;

import com.sparta.productserve.domain.product.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
