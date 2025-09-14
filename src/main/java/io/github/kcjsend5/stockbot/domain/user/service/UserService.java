package io.github.kcjsend5.stockbot.domain.user.service;

import io.github.kcjsend5.stockbot.domain.user.domain.User;
import io.github.kcjsend5.stockbot.domain.user.dto.request.LogInRequest;
import io.github.kcjsend5.stockbot.domain.user.dto.request.RefreshTokenRequest;
import io.github.kcjsend5.stockbot.domain.user.dto.request.SignUpRequest;
import io.github.kcjsend5.stockbot.domain.user.dto.response.LogInResponse;
import io.github.kcjsend5.stockbot.domain.user.dto.response.TokenResponse;
import io.github.kcjsend5.stockbot.domain.user.repository.UserRepository;
import io.github.kcjsend5.stockbot.global.config.SecurityConfig;
import io.github.kcjsend5.stockbot.global.exception.email.DuplicateEmailException;
import io.github.kcjsend5.stockbot.global.exception.email.InvalidEmailException;
import io.github.kcjsend5.stockbot.global.exception.password.InvalidPasswordException;
import io.github.kcjsend5.stockbot.global.exception.token.InvalidTokenException;
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
            throw new DuplicateEmailException();
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

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(InvalidEmailException::new);

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            JwtToken token = provider.generateToken(request.getEmail());
            String accessToken = token.getAccessToken();
            String refreshToken = token.getRefreshToken();

            return new LogInResponse(accessToken,refreshToken,user.getUserName());
        } else{
            throw new InvalidPasswordException();
        }
    }

    public TokenResponse recreateToken(RefreshTokenRequest request){

        String refreshToken = request.getRefreshToken();

        if(!provider.validateRefreshToken(refreshToken)){
            throw new InvalidTokenException();//전역 커스텀 예외 처리하기 throw new InvalidTokenException()
        }

        String email = provider.getEmailFromToken(refreshToken);
        JwtToken token = provider.generateToken(email);

        return new TokenResponse(token.getAccessToken(), token.getRefreshToken());

    }
}
