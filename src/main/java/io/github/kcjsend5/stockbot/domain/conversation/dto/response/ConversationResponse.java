package io.github.kcjsend5.stockbot.domain.conversation.dto.response;

import io.github.kcjsend5.stockbot.domain.knowledge.domain.Knowledge;
import io.github.kcjsend5.stockbot.domain.knowledge.dto.response.KnowledgeResponse;
import io.github.kcjsend5.stockbot.domain.message.dto.response.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private Long id;
    private List<MessageResponse> messages;
    private KnowledgeResponse knowledge;
    private String conversationId;
    private String chatFlowId;
    private String conversationName;

}
