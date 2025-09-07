package io.github.kcjsend5.stockbot.global.jwt;

import io.github.kcjsend5.stockbot.global.Dao.RedisDao;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String GRANT_TYPE = "Bearer";

    private final Key key;
    private final UserDetailsService userDetailsService;
    private final RedisDao redisDao;

    @Value("${jwt.expiration.accessToken}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.expiration.refreshToken}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtTokenProvider(@Value("${jwt.secret:Zm91cnRoc2V0dGxlb25jZWJhZ2Z1bmN0aW9ubGFrZWNvbWluZ3BlcnNvbmY=}") String secretKey,UserDetailsService userDetailsService,RedisDao redisDao){
        this.userDetailsService = userDetailsService;
        this.redisDao = redisDao;
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String email, String authorities, Date expireDate){
        return Jwts.builder()
                .setSubject(email)
                .claim("auth",authorities)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email,Date exprieDate){
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(exprieDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtToken generateToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());
        long now = (new Date()).getTime();
        String email = authentication.getName();

        Date accessTokenExpire = new Date(now+ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = generateAccessToken(email, authorities, accessTokenExpire);

        Date refreshTokenExpire = new Date(now+REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = generateRefreshToken(email, refreshTokenExpire);

        redisDao.setValues(email,refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));

        return JwtToken.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

}
