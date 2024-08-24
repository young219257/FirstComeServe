package com.sparta.firstseversystem.domain.user.service;

import com.sparta.firstseversystem.domain.user.dto.SignupDto;
import com.sparta.firstseversystem.domain.user.entity.User;
import com.sparta.firstseversystem.domain.user.repository.UserRepository;
import com.sparta.firstseversystem.domain.wishlist.entity.WishList;
import com.sparta.firstseversystem.domain.wishlist.repository.WishListRepository;
import com.sparta.firstseversystem.global.exception.DuplicateResourceException;
import com.sparta.firstseversystem.global.exception.ErrorCode;
import com.sparta.firstseversystem.global.exception.NotfoundResourceException;
import com.sparta.firstseversystem.global.security.utils.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final WishListRepository wishlistRepository;


    //회원가입
    @Override
    public void signup(SignupDto requestDto) {

        if(userRepository.findByEmail(requestDto.getEmail()) != null){
            throw new DuplicateResourceException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = User.of(requestDto,encoder);
        userRepository.save(user);

        //회원가입과 동시에 wishlist 생성
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
    public boolean verifyEmail(String token) {
        String email = emailService.getEmailByToken(token);

        if (email != null) {
            User user = userRepository.findByEmail(EncryptionUtils.encrypt(email)).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_USER));
            if (user != null) {
                user.setEmailVerified(true);
                userRepository.save(user);
                redisTemplate.delete(token);
                return true;
            }
        }

        return false;
    }




}
