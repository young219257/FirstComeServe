package com.sparta.orderserve.global.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // ✅ Kafka 성능 최적화 설정 추가
        configProps.put(ProducerConfig.ACKS_CONFIG, "0");  // 리더 브로커만 확인 후 응답
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5); // 5ms 동안 대기 후 배치 전송
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 16KB 배치 크기
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000); // 최대 10초 대기
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 15000); // 15초 후 타임아웃

        return new DefaultKafkaProducerFactory<>(configProps);
    }

}
