package com.sparta.firstseversystem.domain.user.service;

import com.sparta.firstseversystem.domain.user.dto.SignupDto;

public interface UserService {

    void signup(SignupDto requestDto);

    boolean verifyEmail(String token);
}
