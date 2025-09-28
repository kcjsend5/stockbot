package io.github.kcjsend5.stockbot.domain.user.controller;

import io.github.kcjsend5.stockbot.domain.user.dto.request.*;
import io.github.kcjsend5.stockbot.domain.user.dto.response.LogInResponse;
import io.github.kcjsend5.stockbot.domain.user.dto.response.TokenResponse;
import io.github.kcjsend5.stockbot.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> userSignUp(@RequestBody SignUpRequest request) throws Exception{
        userService.userSignUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponse> userLogin(LogInRequest request) throws Exception{
        return ResponseEntity.ok(userService.userLogin(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> recreateToken(RefreshTokenRequest request){
        return ResponseEntity.ok(userService.recreateToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(LogoutRequest request) {
        userService.userLogout(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/setRole")
    public ResponseEntity<Void> setRole(RoleRequest request){
        userService.userRole(request);
        return ResponseEntity.ok().build();
    }

}
