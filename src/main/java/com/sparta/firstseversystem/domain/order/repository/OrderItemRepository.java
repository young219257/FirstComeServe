package com.sparta.firstseversystem.domain.order.repository;

import com.sparta.firstseversystem.domain.order.entity.Order;
import com.sparta.firstseversystem.domain.order.entity.OrderItem;
import com.sparta.firstseversystem.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
