package com.sparta.productserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProductServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServeApplication.class, args);
    }

}
