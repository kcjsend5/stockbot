package io.github.kcjsend5.stockbot.domain.user.dto.request;

import io.github.kcjsend5.stockbot.type.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoleRequest {

    private String email;
    private Role role;

}
