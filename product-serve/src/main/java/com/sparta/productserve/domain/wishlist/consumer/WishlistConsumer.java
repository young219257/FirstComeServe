package com.sparta.productserve.domain.wishlist.consumer;


import com.sparta.productserve.domain.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistConsumer {

    private final WishListService wishListService;

    @KafkaListener(topics = "signup", groupId = "product_group")
    public void createWishlist(ConsumerRecord<String, String> record) {
        try {
            String userId = record.value();
            log.info("회원 Id: {}", userId);
            // wishListService를 호출하여 위시리스트 생성
            wishListService.createWishList(Long.valueOf(userId));
        } catch (Exception e) {
            log.error("Error while processing message: {}", e.getMessage(), e);
        }
    }


}
