package io.github.kcjsend5.stockbot.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class LogoutRequest {

    private String email;

    private String password;

}
