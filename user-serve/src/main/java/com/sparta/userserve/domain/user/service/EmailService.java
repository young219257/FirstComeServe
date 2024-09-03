package com.sparta.userserve.domain.user.service;


import jakarta.mail.internet.MimeMessage;

public interface EmailService {
    String createToken();

    //이메일 형식 메서드
    MimeMessage createMimeMessage(String email, String token);

    //이메일 전송 메서드
    void sendEmail(String toEmail);

    //이메일 토큰 가져오는 메소드
    String getEmailByToken(String token);
}
