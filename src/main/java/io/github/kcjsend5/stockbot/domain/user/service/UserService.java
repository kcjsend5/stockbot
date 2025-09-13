package io.github.kcjsend5.stockbot.domain.user.service;

import io.github.kcjsend5.stockbot.domain.user.domain.User;
import io.github.kcjsend5.stockbot.domain.user.dto.request.LogInRequest;
import io.github.kcjsend5.stockbot.domain.user.dto.request.RefreshTokenRequest;
import io.github.kcjsend5.stockbot.domain.user.dto.request.SignUpRequest;
import io.github.kcjsend5.stockbot.domain.user.dto.response.LogInResponse;
import io.github.kcjsend5.stockbot.domain.user.dto.response.TokenResponse;
import io.github.kcjsend5.stockbot.domain.user.repository.UserRepository;
import io.github.kcjsend5.stockbot.global.config.SecurityConfig;
import io.github.kcjsend5.stockbot.global.jwt.JwtToken;
import io.github.kcjsend5.stockbot.global.jwt.JwtTokenProvider;
import io.github.kcjsend5.stockbot.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider provider;

    @Transactional
    public void userSignUp(SignUpRequest request){

        String password = passwordEncoder.encode(request.getPassword());

        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");//전역 커스텀 예외 처리하기
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(password)
                .userName(request.getUserName())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public LogInResponse userLogin(LogInRequest request){

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new IllegalArgumentException("이메일로 유저 조회 실패"));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            JwtToken token = provider.generateToken(request.getEmail());
            String accessToken = token.getAccessToken();
            String refreshToken = token.getRefreshToken();

            return new LogInResponse(accessToken,refreshToken,user.getUserName());
        } else{
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");//전역 커스텀 예외 처리하기
        }
    }

    public TokenResponse recreateToken(RefreshTokenRequest request){

        String refreshToken = request.getRefreshToken();

        if(!provider.validateRefreshToken(refreshToken)){
            throw new IllegalArgumentException("토큰 값이 다릅니다.");//전역 커스텀 예외 처리하기
        }

        String email = provider.getEmailFromToken(refreshToken);
        JwtToken token = provider.generateToken(email);

        return new TokenResponse(token.getAccessToken(), token.getRefreshToken());

    }
}
