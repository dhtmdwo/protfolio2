package com.example.appapi.users;

import com.example.appapi.users.model.Users;
import com.example.appapi.users.model.UsersDto;
import com.example.appapi.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.appapi.users.model.UserType.CLIENT;

@RequiredArgsConstructor
@Service
public class KakaoService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public String kakaoLogin(String code) {
        String userInfo = HttpClientUtil.getUserInfo(code);
        String userKakaoId = userInfo.split("\"id\":")[1].split(",")[0];

        if(userKakaoId != null) {
            if (usersRepository.existsByUserId("kakao"+userKakaoId)) {
                return userKakaoId;
            } else {
                Users user = new Users();

                user.setUserId("kakao"+userKakaoId);
                user.setUserType(CLIENT);
                user.setName(userInfo.split("\"nickname\":\"")[1].split("\"")[0]);
                user.setPassword(passwordEncoder.encode("kakao@" + userKakaoId));
                usersRepository.save(user);

                String kakaoId = "kakao"+userKakaoId;
                if(usersRepository.existsByUserId(kakaoId)) {
                    return userKakaoId;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }

    }
}
