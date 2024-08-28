package com.sparta.orderserve.domain.order.repository;

import com.sparta.orderserve.domain.order.entity.Order;
import com.sparta.orderserve.domain.order.type.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    Page<Order> findAllByUserId(Long userId, Pageable pageable);
}
