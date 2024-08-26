package com.sparta.userserve.domain.delivery.repository;

import com.sparta.userserve.domain.delivery.entity.Delivery;
import com.sparta.userserve.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
