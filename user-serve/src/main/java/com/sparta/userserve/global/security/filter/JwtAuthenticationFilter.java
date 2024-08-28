package com.sparta.userserve.global.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.userserve.global.security.service.CustomUserDetails;
import com.sparta.userserve.global.security.service.CustomUserDetailsService;
import com.sparta.userserve.global.security.utils.EncryptionUtils;
import com.sparta.userserve.global.security.utils.JwtUtils;

import com.sparta.userserve.domain.user.dto.LoginRequestDto;
import com.sparta.userserve.global.security.service.CustomUserDetails;
import com.sparta.userserve.global.security.service.CustomUserDetailsService;
import com.sparta.userserve.global.security.utils.EncryptionUtils;
import com.sparta.userserve.global.security.utils.JwtUtils;
import com.sparta.userserve.domain.user.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Slf4j(topic = "로그인 및 JWT accessToken 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   AuthenticationManager authenticationManager,
                                   CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");


        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(EncryptionUtils.encrypt(requestDto.getEmail()),requestDto.getPassword()));
        } catch (IOException e) {
            log.error("로그인 요청 처리 중 오류 발생", e);
            throw new AuthenticationServiceException("로그인 요청 처리 중 오류 발생", e);
        }

    }



    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {


        String email = ((CustomUserDetails) authResult.getPrincipal()).getUsername();
        String accessToken = jwtUtils.createAccessToken(email);

        // Authorization 헤더에 JWT 토큰 추가
        response.addHeader("Authorization", "Bearer " + accessToken);

        // JSON 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 형태로 응답 작성 (로그인 성공 메시지 포함)
        String jsonResponse = String.format("{\"message\": \"로그인에 성공하셨습니다.\", \"accessToken\" : \"Bearer %s\"}", accessToken);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"message\": \"로그인에 실패하셨습니다. " + failed.getMessage() + "\"}";
        response.getWriter().write(jsonResponse);
    }


}
