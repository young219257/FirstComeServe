package com.sparta.firstseversystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FirstSeverSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstSeverSystemApplication.class, args);
    }

}
