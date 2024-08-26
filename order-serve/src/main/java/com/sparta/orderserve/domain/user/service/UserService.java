package com.sparta.orderserve.domain.user.service;

import com.sparta.orderserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.orderserve.domain.user.dto.SignupDto;
import com.sparta.orderserve.domain.user.entity.User;

public interface UserService {

    void signup(SignupDto requestDto);

    boolean verifyEmail(String token);

//    void logout(User user, HttpServletRequest request);

    void updateUser(User user, PasswordUpdateRequestDto requestDto);
}
