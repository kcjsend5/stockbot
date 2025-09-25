package io.github.kcjsend5.stockbot.domain.message.dto.response;

import io.github.kcjsend5.stockbot.type.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Long messageId;
    private String messageText;
    private Sender sender;

}
