package com.sparta.userserve.domain.user.entity;

import com.sparta.userserve.domain.user.dto.SignupDto;
import com.sparta.userserve.global.security.utils.EncryptionUtils;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Entity
@Getter
@Table(name="users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean emailVerified;

//    Instead @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
//    private List<Order> orders;

//    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
//    private WishList wishList;


    public static User of(SignupDto requestDto, BCryptPasswordEncoder encoder){

        //비밀번호 : 단방향 암호화
        //그 외 개인 정보 : 양방향 암호화
        return User.builder()
                .username(EncryptionUtils.encrypt(requestDto.getName()))
                .email(EncryptionUtils.encrypt(requestDto.getEmail()))
                .password(encoder.encode(requestDto.getPassword()))
                .address(EncryptionUtils.encrypt(requestDto.getAddress()))
                .phoneNumber(EncryptionUtils.encrypt(requestDto.getPhoneNumber()))
                .emailVerified(false)
                .build();
    }

    public void setEmailVerified(boolean b) {
        this.emailVerified = b;
    }

    //비밀번호 업데이트 메소드
    public void updatePassword(String newPassword) {
        this.password=newPassword;
    }
}
