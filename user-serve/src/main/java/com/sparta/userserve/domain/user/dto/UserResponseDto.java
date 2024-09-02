package com.sparta.userserve.domain.user.dto;


import com.sparta.userserve.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String username;

    private String email;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder().
                id(user.getId()).
                username(user.getUsername()).
                email(user.getEmail()).build();
    }
}
