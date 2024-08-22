package com.sparta.firstseversystem.user.service;

import com.sparta.firstseversystem.user.dto.SignupDto;

public interface UserService {

    void signup(SignupDto requestDto);

    boolean verifyEmail(String token);
}
