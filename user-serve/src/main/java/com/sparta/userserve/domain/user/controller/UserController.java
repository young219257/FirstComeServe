package com.sparta.userserve.domain.user.controller;

import com.sparta.userserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.userserve.domain.user.dto.SignupDto;
import com.sparta.userserve.domain.user.service.UserService;
import com.sparta.userserve.global.exception.ErrorCode;
import com.sparta.userserve.global.exception.InvalidateTokenException;
import com.sparta.userserve.global.exception.handler.dto.ApiResponse;
import com.sparta.userserve.global.security.service.CustomUserDetails;
import com.sparta.userserve.global.exception.ErrorCode;
import com.sparta.userserve.global.exception.InvalidateTokenException;
import com.sparta.userserve.global.exception.handler.dto.ApiResponse;
import com.sparta.userserve.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
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
    @GetMapping("/verify")
    public ApiResponse varifyEmail(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyEmail(token);
        if (isVerified) {
            return ApiResponse.ok(200,"이메일 인증이 완료되었습니다.");
        }
        else{
            throw new InvalidateTokenException(ErrorCode.VALIDATE_TOKEN);
        }
    }


//    /**로그아웃 api
//     @Param : userId
//     @return : 성공 여부
//     **/
//    @PostMapping("/logout")
//    public ApiResponse logout(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
//        userService.logout(userDetails.getUser(),request);
//        return ApiResponse.ok(200,"로그아웃에 성공하셨습니다.");
//    }


    /**회원 수정 api
     method : put
     @Param : userId
     @return : 성공 여부
     **/
    @PutMapping("/modify/password")
    public ApiResponse updateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @RequestBody PasswordUpdateRequestDto requestDto) {
        userService.updateUser(customUserDetails.getUser(),requestDto);
        return ApiResponse.ok(200,"회원정보 수정에 성공하셨습니다.");
    }

}