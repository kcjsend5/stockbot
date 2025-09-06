package io.github.kcjsend5.stockbot.global.jwt;

import io.github.kcjsend5.stockbot.global.Dao.RedisDao;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final UserDetailsService userDetailsService;
    private final RedisDao redisDao;

    @Value("${jwt.expiration.accessToken}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.expiration.refreshToken}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,UserDetailsService userDetailsService,RedisDao redisDao){
        this.userDetailsService = userDetailsService;
        this.redisDao = redisDao;
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
