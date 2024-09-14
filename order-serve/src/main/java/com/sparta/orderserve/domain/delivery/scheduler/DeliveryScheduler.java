package com.sparta.orderserve.domain.delivery.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.orderserve.domain.delivery.type.DeliveryStatus;
import com.sparta.orderserve.domain.order.dto.StockUpdateDto;
import com.sparta.orderserve.domain.order.entity.Order;
import com.sparta.orderserve.domain.order.entity.OrderItem;
import com.sparta.orderserve.domain.order.producer.OrderProducer;
import com.sparta.orderserve.domain.order.repository.OrderRepository;
import com.sparta.orderserve.domain.order.type.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryScheduler {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;


    @Scheduled(cron = "0 0 12,18 * * *") // 매일 12시, 18시
    public void updateDelivery() {
        log.info("배송 시작일, 완료일 설정, 상태 배송 시작, 완료로 전환");
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            updateOrderDeliveryStatus(order);
        }
    }

    private void updateOrderDeliveryStatus(Order order) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = order.getCreatedAt();
        if (now.isAfter(createdAt.plusDays(2))) {
            setOrderCompleteDelivery(order);
        } else if (now.isAfter(createdAt.plusDays(1))) {
            setOrderDeliverying(order);
        }
    }

    private void setOrderDeliverying(Order order) {
        order.getDelivery().setDeliveryStatus(DeliveryStatus.DELIVERYING);
        order.getDelivery().setStartedAt(LocalDateTime.now());
    }

    private void setOrderCompleteDelivery(Order order) {
        order.getDelivery().setDeliveryStatus(DeliveryStatus.COMPLETE_DELIVERY);
        order.getDelivery().setCompletedAt(LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 14,20 * * *") // 매일 14시, 20시
    public void updateStockAndOrderStatus() throws JsonProcessingException {
        log.info("반품 신청으로부터 1일 후 재고 복구, 주문 상태 전환");
        List<Order> orders = orderRepository.findAllByOrderStatus(OrderStatus.SIGN_RETURN);
        for (Order order : orders) {
            if (shouldUpdateStock(order)) {
                processReturn(order);
            }
        }
    }

    private boolean shouldUpdateStock(Order order) {
        return LocalDateTime.now().isAfter(order.getReturnSignedAt().plusDays(1));
    }

    private void processReturn(Order order) throws JsonProcessingException {

        List<StockUpdateDto> stockUpdateDtos =new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            //주문 상품들에 대한 재고 복구
            StockUpdateDto stockUpdateDto = StockUpdateDto.from(orderItem);
            stockUpdateDtos.add(stockUpdateDto);
        }
        orderProducer.returnOrder(stockUpdateDtos);
        order.setOrderStatus(OrderStatus.RETURN); // 변경된 상태
    }

}
