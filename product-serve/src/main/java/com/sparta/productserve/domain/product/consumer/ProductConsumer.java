package com.sparta.productserve.domain.product.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.productserve.domain.product.dto.StockUpdateDto;
import com.sparta.productserve.domain.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductConsumer {

    private ProductService productService;
    private ObjectMapper mapper;

    public ProductConsumer(ProductService productService) {
        this.productService = productService;
        this.mapper = new ObjectMapper();
    }

    @KafkaListener(topics={"complete_order","delete_order","return_order"}, groupId = "order_group")
    public void addOrder(ConsumerRecord<String, String> record) {
        log.info("Received Kafka message on topic: {}, value: {}", record.topic(), record.value()); // 로그 추가

        try {
            // Json을 객체로 변경
            List<StockUpdateDto> stockUpdateDtos =
                    mapper.readValue(record.value(), new TypeReference<List<StockUpdateDto>>() {});

            switch (record.topic()) {
                case "complete_order":
                    updateStock(stockUpdateDtos);
                    break;
                case "delete_order", "return_order":
                    undoStock(stockUpdateDtos);
                    break;
            }
        } catch (Exception e) {
            log.error("🚨Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private void updateStock(List<StockUpdateDto> stockUpdateDtos) {
        for (StockUpdateDto dto : stockUpdateDtos) {
            productService.updateProductStock(dto);
        }
    }

    private void undoStock(List<StockUpdateDto> stockUpdateDtos) {
        for (StockUpdateDto dto : stockUpdateDtos) {
            productService.undoProductStock(dto);
        }
    }
}