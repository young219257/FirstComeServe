package com.sparta.productserve.domain.user.service;

import com.sparta.productserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.productserve.domain.user.dto.SignupDto;
import com.sparta.productserve.domain.user.entity.User;

public interface UserService {

    void signup(SignupDto requestDto);

    boolean verifyEmail(String token);

//    void logout(User user, HttpServletRequest request);

    void updateUser(User user, PasswordUpdateRequestDto requestDto);
}
