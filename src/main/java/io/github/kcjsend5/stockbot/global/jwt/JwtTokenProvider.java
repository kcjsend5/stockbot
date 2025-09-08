package io.github.kcjsend5.stockbot.global.jwt;

import io.github.kcjsend5.stockbot.global.Dao.RedisDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
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

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,UserDetailsService userDetailsService,RedisDao redisDao){
        this.userDetailsService = userDetailsService;
        this.redisDao = redisDao;
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String email, Date expireDate){
        return Jwts.builder()
                .setSubject(email)
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

    public JwtToken generateToken(String email){

        long now = (new Date()).getTime();

        Date accessTokenExpire = new Date(now+ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = generateAccessToken(email, accessTokenExpire);

        Date refreshTokenExpire = new Date(now+REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = generateRefreshToken(email, refreshTokenExpire);

        redisDao.setValues(email,refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));

        return JwtToken.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public Authentication getAuthentication(String accessToken){

        Claims claims = parseClaims(accessToken);
        if(claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)//암호화한 키로 다시 복호화
                    .build()
                    .parseClaimsJwt(accessToken)//토큰 검증, 검증 후 파싱: Jwts 토큰을 Header, Body, Signature 세 부분으로 분리
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

}
