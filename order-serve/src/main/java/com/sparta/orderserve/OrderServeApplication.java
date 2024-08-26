package com.sparta.orderserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OrderServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServeApplication.class, args);
    }

}
