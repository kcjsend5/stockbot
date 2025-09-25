package io.github.kcjsend5.stockbot.domain.conversation.dto.response;

import io.github.kcjsend5.stockbot.domain.message.dto.response.MessageResponse;
import io.github.kcjsend5.stockbot.type.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessageResponse {

    private MessageResponse messageResponse;
    private Sender sender;
    private String conversationId;

}
