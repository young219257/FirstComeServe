package com.sparta.paymentserve.domain.producer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentProducer {

    @Value("${payment.complete.topic}")
    private String PAYMENT_COMPLETE_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;
    public PaymentProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void approvedPayment(Long orderId) {
        try{
            kafkaTemplate.send(PAYMENT_COMPLETE_TOPIC, orderId.toString());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
