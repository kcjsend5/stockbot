package io.github.kcjsend5.stockbot.domain.conversation.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConversationListResponse {

    private List<String> conversationIds;
    private List<String> conversationNames;

}
