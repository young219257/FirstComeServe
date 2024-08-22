package com.sparta.firstseversystem.global.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    //이메일 토큰을 저장하기 위한 redis 파일
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender javaMailSender;



    //토큰 생성 메서드
    @Override
    public String createToken(){
        return UUID.randomUUID().toString();
    }
    //이메일 형식 메서드
    @Override
    public MimeMessage createMimeMessage(String email, String token) {

        String setFrom = "young219257@naver.com";
        String toMail = email;
        String title = "이메일 인증 링크";
        String content = "이메일 인증 링크를 클릭하세요: " +
                "<a href=\"http://localhost:8080/verify?token=" + token + "\">인증 링크</a>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(setFrom); // 발신자 이메일
            helper.setTo(toMail); // 수신자 이메일
            helper.setSubject(title); // 이메일 제목
            helper.setText(content, true); // 이메일 내용 (HTML 사용)

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return mimeMessage;
    }
    //이메일 전송 메서드
    @Override
    public void sendEmail(String toEmail){

        // 이메일 주소 형식이 유효한지 확인
        if (!isValidEmailAddress(toEmail)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다: " + toEmail);
        }
        String token = createToken();
        MimeMessage mimeMessage = createMimeMessage(toEmail,token);
        javaMailSender.send(mimeMessage);

        //토큰을 redis에 저장(키 : 토큰, 값: 이메일 주소)
        redisTemplate.opsForValue().set("email", toEmail, Duration.ofMinutes(10));

    }

    //이메일을 가져오는 메소드
    @Override
    public String getEmailByToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }
    private boolean isValidEmailAddress(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
