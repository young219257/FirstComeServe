package com.sparta.firstseversystem.user.controller;

import com.sparta.firstseversystem.global.exception.handler.dto.ApiResponse;
import com.sparta.firstseversystem.user.dto.SignupDto;
import com.sparta.firstseversystem.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    //회원가입 api
    @PostMapping("/signup")
    public ApiResponse signup(@RequestBody SignupDto requestDto) {
        userService.signup(requestDto);
        return ApiResponse.ok(200,"회원가입 성공");

    }

    //이메일 인증 완료 시 브라우저에서 서버로 get요청
    @GetMapping("/varify")
    public ApiResponse varifyEmail(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyEmail(token);
        if (isVerified) {
            return ApiResponse.ok(200,"이메일 인증이 완료되었습니다.");
        }
        else{
            return ApiResponse.error(400,"유효하지 않은 토큰입니다.");
        }
    }

    /**로그인 api
     @Param : email, password
     @return : 성공 여부
     **/


    /**로그아웃 api
     @Param : userId
     @return : 성공 여부
     **/

    /**회원 수정 api
     method : put
     @Param : userId
     @return : 성공 여부
     **/

}