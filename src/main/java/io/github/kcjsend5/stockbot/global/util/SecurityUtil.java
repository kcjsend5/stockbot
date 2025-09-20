package io.github.kcjsend5.stockbot.global.util;


import io.github.kcjsend5.stockbot.domain.user.domain.User;
import io.github.kcjsend5.stockbot.domain.user.repository.UserRepository;
import io.github.kcjsend5.stockbot.global.exception.token.InvalidTokenException;
import io.github.kcjsend5.stockbot.global.exception.user.UserNotFoundException;
import io.github.kcjsend5.stockbot.global.jwt.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class SecurityUtil {

    public static String getCurrentEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null||authentication.getName()==null){
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        return authentication.getName();
    }

    public static Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null||authentication.getPrincipal()==null||!authentication.isAuthenticated()){
            throw new InvalidTokenException();
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getId();
    }

}
