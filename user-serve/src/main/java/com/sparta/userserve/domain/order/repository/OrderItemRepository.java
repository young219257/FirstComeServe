package com.sparta.userserve.domain.order.repository;

import com.sparta.userserve.domain.order.entity.OrderItem;
import com.sparta.userserve.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
