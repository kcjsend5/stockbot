package io.github.kcjsend5.stockbot.global.jwt;

import io.github.kcjsend5.stockbot.global.dao.RedisDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

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
        UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());
        if(principal.getAuthorities() == null||principal.getAuthorities().isEmpty()){
            throw new InsufficientAuthenticationException("권한 정보가 없는 토큰입니다");
        }

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    private Claims parseClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)//암호화한 키로 다시 복호화
                    .build()
                    .parseClaimsJwt(token)//토큰 검증, 검증 후 파싱: Jwts 토큰을 Header, Body, Signature 세 부분으로 분리
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public String getEmailFromToken(String token){

        try {
            Claims claims = parseClaims(token);
            return claims.getSubject();
        } catch (ExpiredJwtException e){// 토큰이 만료되어도 클레임 내용을 가져올 수 있음
            return e.getClaims().getSubject();
        }
    }

    //토큰 정보 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)//암호화한 키로 다시 복호화
                    .build()
                    .parseClaimsJwt(token);

            return true;
        } catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e){
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT Token",e);
        } catch (IllegalArgumentException e){
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    public boolean validateRefreshToken(String token){
        if(!validateToken(token)){
            return false;
        }

        try{
            String email = getEmailFromToken(token);
            String redisToken = (String)redisDao.getValues(email);
            return token.equals(redisToken);
        } catch (Exception e){
            log.info("RefreshToken Validation Failed", e);
            return false;
        }
    }

    public void deleteRefreshToken(String email){
        if(email == null||email.isBlank()){
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        redisDao.deleteValues(email);
    }

}
