package com.sparta.firstseversystem.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

    private String name;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
}
