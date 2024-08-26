package com.sparta.productserve.domain.user.service;

import com.sparta.productserve.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.productserve.domain.user.dto.SignupDto;
import com.sparta.productserve.domain.user.entity.User;
import com.sparta.productserve.domain.user.repository.UserRepository;
import com.sparta.productserve.domain.wishlist.entity.WishList;
import com.sparta.productserve.domain.wishlist.repository.WishListRepository;
import com.sparta.productserve.global.exception.DuplicateResourceException;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.InvalidPasswordException;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import com.sparta.productserve.global.security.utils.EncryptionUtils;
import com.sparta.productserve.global.security.utils.JwtUtils;
import com.sparta.productserve.domain.wishlist.entity.WishList;
import com.sparta.productserve.domain.wishlist.repository.WishListRepository;
import com.sparta.productserve.global.exception.DuplicateResourceException;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.InvalidPasswordException;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import com.sparta.productserve.global.security.utils.EncryptionUtils;
import com.sparta.productserve.global.security.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final long TOKEN_TTL = 3600000L; // 1 hour in milliseconds

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final WishListRepository wishlistRepository;
    private final JwtUtils jwtUtils;



    //회원가입
    @Override
    @Transactional
    public void signup(SignupDto requestDto) {

        //이메일 중복 체크
        checkEmailDuplication(requestDto.getEmail());

        User user = User.of(requestDto,encoder);
        userRepository.save(user);

        //wishlist 생성
        WishList wishlist = WishList.builder()
                .user(user)
                .build();
        wishlistRepository.save(wishlist);

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
                user.setEmailVerified(true);
                userRepository.save(user);
                redisTemplate.delete(token);
                return true;
            }
        }

        return false;
    }

//    @Override
//    @Transactional
//    public void logout(User user, HttpServletRequest request) {
//
//        String token = jwtUtils.getJwtFromHeader(request).substring(7);
//        accessTokenBlackListService.addToBlackList(token,TOKEN_TTL);
//    }

    @Override
    @Transactional
    public void updateUser(User user, PasswordUpdateRequestDto requestDto) {
        if(!encoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.updatePassword(encoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);

        // 비밀번호 변경 시 모든 기기에서 로그아웃

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
