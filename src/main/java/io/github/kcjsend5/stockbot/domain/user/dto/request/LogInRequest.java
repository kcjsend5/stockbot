package io.github.kcjsend5.stockbot.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogInRequest {

    private String email;//로그인 아이디

    private String password;

}
