package com.sparta.orderserve.domain.order.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.orderserve.domain.order.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class OrderConsumer {

    private final OrderService orderService;
    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics="payment_complete_topic",groupId = "payment_group")
    public void updateOrderStatus(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String orderId = record.value();
        orderService.completeOrder(Long.valueOf(orderId));
    }
}
