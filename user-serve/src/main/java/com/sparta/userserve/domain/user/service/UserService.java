package com.sparta.userserve.domain.user.service;

import com.sparta.userserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.userserve.domain.user.dto.SignupDto;
import com.sparta.userserve.domain.user.dto.UserResponseDto;
import com.sparta.userserve.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import reactor.core.publisher.Mono;

public interface UserService {

    void signup(SignupDto requestDto);

    boolean verifyEmail(String token);

    void logout(Long userId, HttpServletRequest request);

    void updateUser(Long userId, PasswordUpdateRequestDto requestDto);

    UserResponseDto getUser(Long userId);
}
