package com.sparta.userserve.global.security.filter;

import com.sparta.userserve.global.security.service.CustomUserDetailsService;
import com.sparta.userserve.global.security.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j(topic = "인증")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private RedisTemplate<String, String> redisTemplate;
    public JwtAuthorizationFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authorization = jwtUtils.getJwtFromHeader(request);
        log.info("Authorization 헤더: {}", authorization);


        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            log.info(token);

            if(token != null && redisTemplate.hasKey(token)) {
                //블랙리스트 있으면 요청 거부
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;

            }
            if(!jwtUtils.validateToken(token)) {
                log.error("Token error");
                filterChain.doFilter(request, response);
                return;
            }

            Claims info = jwtUtils.getUserInfoFromToken(token);
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        // 필터 체인을 계속 진행시킵니다.
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username){
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}