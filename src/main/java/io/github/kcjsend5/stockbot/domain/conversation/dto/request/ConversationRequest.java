package io.github.kcjsend5.stockbot.domain.conversation.dto.request;

import io.github.kcjsend5.stockbot.type.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {

    private String email;
    private String conversationName;
    private Category category;

}
