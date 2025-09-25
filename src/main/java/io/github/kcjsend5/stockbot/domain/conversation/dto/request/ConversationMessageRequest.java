package io.github.kcjsend5.stockbot.domain.conversation.dto.request;

import io.github.kcjsend5.stockbot.domain.message.dto.response.MessageResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConversationMessageRequest {

    private String conversationId;
    private String query;

}
