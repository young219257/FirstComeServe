package com.sparta.userserve.domain.user.service;

import com.sparta.userserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.userserve.domain.user.dto.SignupDto;
import com.sparta.userserve.domain.user.entity.User;
import com.sparta.userserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.userserve.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    void signup(SignupDto requestDto);

    boolean verifyEmail(String token);

    void logout(User user, HttpServletRequest request);

    void updateUser(User user, PasswordUpdateRequestDto requestDto);
}
