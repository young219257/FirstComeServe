package com.sparta.userserve.global.security.service;

import com.sparta.userserve.domain.user.entity.User;
import com.sparta.userserve.domain.user.repository.UserRepository;
import com.sparta.userserve.global.exception.ErrorCode;
import com.sparta.userserve.domain.user.entity.User;
import com.sparta.userserve.domain.user.repository.UserRepository;
import com.sparta.userserve.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CustomUserDetailsService")
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.NOTFOUND_USER.getMessage()));
        return new CustomUserDetails(user);
    }
}
