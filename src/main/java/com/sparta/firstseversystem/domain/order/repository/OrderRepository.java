package com.sparta.firstseversystem.domain.order.repository;

import com.sparta.firstseversystem.domain.order.entity.Order;
import com.sparta.firstseversystem.domain.order.type.OrderStatus;
import com.sparta.firstseversystem.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUser(User user, Pageable pageable);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
