package com.sparta.productserve.domain.product.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.productserve.domain.product.dto.ProductStockUpdateDto;
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


        try {
            // Json을 객체로 변경
            List<ProductStockUpdateDto> productStockUpdateDtos =
                    mapper.readValue(record.value(), new TypeReference<List<ProductStockUpdateDto>>() {});

            switch (record.topic()) {
                case "complete_order":
                    updateStock(productStockUpdateDtos);
                    break;
                case "delete_order":
                    undoStock(productStockUpdateDtos);
                    break;
                case "return_order":
                    undoStock(productStockUpdateDtos);
                    break;

            }

        } catch (Exception e) {
            log.error("Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }

    }


    // 재고 업데이트
    private void updateStock(List<ProductStockUpdateDto> productStockUpdateDtos) {
        for (ProductStockUpdateDto dto : productStockUpdateDtos) {
            productService.updateProductStock(dto);
        }
    }

    // 재고 되돌리기
    private void undoStock(List<ProductStockUpdateDto> productStockUpdateDtos) {
        for (ProductStockUpdateDto dto : productStockUpdateDtos) {
            productService.undoProductStock(dto);
        }
    }

}
