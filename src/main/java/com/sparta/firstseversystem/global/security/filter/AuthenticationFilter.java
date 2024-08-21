package com.sparta.firstseversystem.global.security.filter;


import com.sparta.firstseversystem.global.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j(topic = "로그인 및 JWT accessToken 생성")
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private final JwtUtils jwtUtils;


    public AuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        setFilterProcessesUrl("/api/members/login");
    }
}
