package io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DifyDeleteRequest {
    private String user;
}
