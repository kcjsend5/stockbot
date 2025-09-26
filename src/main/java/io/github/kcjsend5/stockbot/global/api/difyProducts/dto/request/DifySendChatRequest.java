package io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request;
import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class DifySendChatRequest {

    private Map<String, Object> inputs;   // 동적 파라미터라 Map으로 두는 게 유연함
    private String query;
    private String response_mode;
    private String conversation_id;
    private String user;

}
