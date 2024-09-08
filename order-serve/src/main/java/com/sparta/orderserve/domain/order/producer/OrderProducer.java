package com.sparta.orderserve.domain.order.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.orderserve.domain.order.dto.ProductUpdateRequestDto;
import com.sparta.orderserve.global.exception.ErrorCode;
import com.sparta.orderserve.global.exception.handler.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderProducer {

    @Value("${order.complete.topic}")
    private String ORDER_COMPLETE_TOPIC;
    @Value("${order.update.topic}")
    private String ORDER_UPDATE_TOPIC;
    @Value("${order.return.topic}")
    private String RETURN_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void completeOrder(List<OrderItemRequestDto> products) throws JsonProcessingException {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString=objectMapper.writeValueAsString(products);
            kafkaTemplate.send(ORDER_COMPLETE_TOPIC, jsonInString);
            log.info("주문 생성 완료");
            log.info(jsonInString);

        }catch (Exception e){
            log.error("Kafka 메시지 전송 중 에러 발생 : {}",e.getMessage());
        }

    }

    public void deleteOrder(List<ProductUpdateRequestDto> products) throws JsonProcessingException {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString=objectMapper.writeValueAsString(products);
            kafkaTemplate.send(ORDER_UPDATE_TOPIC, jsonInString);

        }catch (Exception e){
            log.error("Kafka 메시지 전송 중 에러 발생 : {}",e.getMessage());
        }
    }

    public void returnOrder(List<ProductUpdateRequestDto> products) throws JsonProcessingException {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString=objectMapper.writeValueAsString(products);
            kafkaTemplate.send(RETURN_TOPIC, jsonInString);

        }catch (Exception e){
            log.error("Kafka 메시지 전송 중 에러 발생 : {}",e.getMessage());
        }
    }
}
