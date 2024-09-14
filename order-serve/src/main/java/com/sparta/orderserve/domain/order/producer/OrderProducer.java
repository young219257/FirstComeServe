package com.sparta.orderserve.domain.order.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.orderserve.domain.order.dto.StockUpdateDto;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderProducer {

    @Value("${order.complete.topic}")
    private String completeTopic;

    @Value("${order.update.topic}")
    private String deleteTopic;

    @Value("${order.return.topic}")
    private String returnTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void completeOrder(List<OrderItemRequestDto> products) throws JsonProcessingException {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString=objectMapper.writeValueAsString(products);
            kafkaTemplate.send(completeTopic, jsonInString);
            log.info("주문 생성 완료");
            log.info(jsonInString);

        }catch (KafkaException e){

            log.error(e.getMessage());
        }

    }

    public void deleteOrder(List<StockUpdateDto> products) throws JsonProcessingException {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString=objectMapper.writeValueAsString(products);
            kafkaTemplate.send(deleteTopic, jsonInString);

        }catch (Exception e){
            log.error("Kafka 메시지 전송 중 에러 발생 : {}",e.getMessage());
        }
    }

    public void returnOrder(List<StockUpdateDto> products) throws JsonProcessingException {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString=objectMapper.writeValueAsString(products);
            kafkaTemplate.send(returnTopic, jsonInString);

        }catch (Exception e){
            log.error("Kafka 메시지 전송 중 에러 발생 : {}",e.getMessage());
        }
    }
}
