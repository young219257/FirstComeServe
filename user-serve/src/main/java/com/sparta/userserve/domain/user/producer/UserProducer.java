package com.sparta.userserve.domain.user.producer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserProducer {

    private static final String TOPIC = "signup";

    private KafkaTemplate<String, String> kafkaTemplate;

    public UserProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }



    public void sendSignupCompleteEvent(Long userId) {

        kafkaTemplate.send(TOPIC, userId.toString());
        log.info("회원가입 완료 userId : {}",userId);
    }
}

