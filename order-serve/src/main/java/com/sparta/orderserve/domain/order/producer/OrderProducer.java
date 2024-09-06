package com.sparta.orderserve.domain.order.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.orderserve.domain.order.dto.ProductUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderProducer {

    //개발 서버, 운영 서버 분리
    //properties로 관리
    //보상트랜잭션
    //분산트랜잭션
    //에러 발생 시 consumer->producer로 보냄..?
    private static final String ORDER_COMPLETE_TOPIC="complete_order";
    private static final String ORDER_UPDATE_TOPIC="delete_order";
    private static final String RETURN_TOPIC="return_order";

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
