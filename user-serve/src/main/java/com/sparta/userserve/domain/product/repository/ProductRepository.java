package com.sparta.userserve.domain.product.repository;


import com.sparta.userserve.domain.product.entity.Product;
import com.sparta.userserve.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
