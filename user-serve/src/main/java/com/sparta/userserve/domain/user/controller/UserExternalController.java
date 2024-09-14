package com.sparta.userserve.domain.user.controller;

import com.sparta.userserve.domain.user.dto.UserResponseDto;
import com.sparta.userserve.domain.user.service.UserService;
import com.sparta.userserve.global.exception.ErrorCode;
import com.sparta.userserve.global.exception.NotfoundResourceException;
import com.sparta.userserve.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/user")
@RequiredArgsConstructor
public class UserExternalController {


    private final UserService userService;


    //회원 정보 가져오는 메소드
    @GetMapping("{userId}")
    public ApiResponse<UserResponseDto> sendUserInfo(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.getUser(userId);
        if(userResponseDto == null){
            throw new NotfoundResourceException(ErrorCode.NOTFOUND_USER);
        }
        else{
            return ApiResponse.ok(200,"회원 정보 조회에 성공하셨습니다.",userResponseDto);
        }
    }
}
