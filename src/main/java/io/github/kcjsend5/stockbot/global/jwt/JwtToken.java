package io.github.kcjsend5.stockbot.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Data
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
