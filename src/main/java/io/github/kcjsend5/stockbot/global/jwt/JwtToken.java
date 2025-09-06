package io.github.kcjsend5.stockbot.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Component
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
