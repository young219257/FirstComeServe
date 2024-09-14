package com.sparta.productserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableKafka
public class ProductServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServeApplication.class, args);
    }

}
