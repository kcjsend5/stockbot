package io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class DifySendChatResponse {
    private String conversation_id;
    private String answer;
}
