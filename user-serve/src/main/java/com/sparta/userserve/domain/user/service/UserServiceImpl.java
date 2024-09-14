package com.sparta.userserve.domain.user.service;

import com.sparta.userserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.userserve.domain.user.dto.SignupDto;
import com.sparta.userserve.domain.user.dto.UserResponseDto;
import com.sparta.userserve.domain.user.entity.User;
import com.sparta.userserve.domain.user.producer.UserProducer;
import com.sparta.userserve.domain.user.repository.UserRepository;
import com.sparta.userserve.global.exception.DuplicateResourceException;
import com.sparta.userserve.global.exception.ErrorCode;
import com.sparta.userserve.global.exception.InvalidPasswordException;
import com.sparta.userserve.global.exception.NotfoundResourceException;
import com.sparta.userserve.global.security.utils.EncryptionUtils;
import com.sparta.userserve.global.security.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtils jwtUtils;
    private final UserProducer userProducer;
    //회원가입
    @Override
    @Transactional
    public void signup(SignupDto requestDto) {

        //이메일 중복 체크
        checkEmailDuplication(requestDto.getEmail());

        User user = User.of(requestDto,encoder);
        userRepository.save(user);

        //이메일 인증을 위한 이메일 전송
        emailService.sendEmail(EncryptionUtils.decrypt(user.getEmail()));


    }


    /**
     * 이메일 링크를 클릭하면 브라우저에서 get 요청을
     *     받아 토큰의 유효성을 검사하는 메소드
     @param : token
     토큰의 유효함을 확인
     유효 : user.getVarified = true
     유효 x : user.getVarified = false
     **/
    @Override
    @Transactional
    public boolean verifyEmail(String token) {
        String email = emailService.getEmailByToken(token);
        if (email != null) {
            User user = findUserByEmail(email);
            if (user != null) {
                user.updateEmailVerified(true);
                userRepository.save(user);
                redisTemplate.delete(token);

                //회원가입 완료 이벤트 발행
                userProducer.sendSignupCompleteEvent(user.getId());
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional
    public void logout(Long userId, HttpServletRequest request) {

        String token = jwtUtils.getJwtFromHeader(request).substring(7);
        Long expiration= jwtUtils.getExpiryTime(token);
        Long remainTime = expiration - System.currentTimeMillis();

        redisTemplate.opsForValue().set(token,"logout",Duration.ofMillis(remainTime));
    }

    @Override
    @Transactional
    public void updateUser(Long userId, PasswordUpdateRequestDto requestDto) {

        User user=findUserById(userId);
        if(!encoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.updatePassword(encoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);

        // 비밀번호 변경 시 모든 기기에서 로그아웃

    }


    @Override
    public UserResponseDto getUser(Long userId) {
        User user=findUserById(userId);
        return UserResponseDto.from(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_USER));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(EncryptionUtils.encrypt(email)).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_USER));

    }
    private void checkEmailDuplication(String email) {
        //이메일 중복 체크
        Optional<User> existingUser = userRepository.findByEmail(EncryptionUtils.encrypt(email));

        if (existingUser.isPresent()) {
            throw new DuplicateResourceException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

}
