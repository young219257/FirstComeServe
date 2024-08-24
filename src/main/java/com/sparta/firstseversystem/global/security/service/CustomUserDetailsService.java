package com.sparta.firstseversystem.global.security.service;

import com.sparta.firstseversystem.domain.user.entity.User;
import com.sparta.firstseversystem.domain.user.repository.UserRepository;
import com.sparta.firstseversystem.global.exception.ErrorCode;
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
