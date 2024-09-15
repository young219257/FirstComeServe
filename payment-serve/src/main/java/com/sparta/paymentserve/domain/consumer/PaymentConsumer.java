package com.sparta.paymentserve.domain.consumer;


import com.sparta.paymentserve.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableKafka
public class PaymentConsumer {


    private final PaymentService paymentService;


    @KafkaListener(topics={"delete_order","return_order"},groupId = "paymentId")
    public void updatePaymentStatus(ConsumerRecord<String, String> record){
        String orderId = record.value();
        paymentService.canceledPayment(Long.valueOf(orderId));
    }
}
