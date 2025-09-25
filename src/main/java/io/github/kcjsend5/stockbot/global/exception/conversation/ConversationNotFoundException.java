package io.github.kcjsend5.stockbot.global.exception.conversation;

import io.github.kcjsend5.stockbot.global.exception.CustomException;
import io.github.kcjsend5.stockbot.global.exception.ErrorCode;

public class ConversationNotFoundException extends CustomException {
    public ConversationNotFoundException(){
        super(ErrorCode.CONVERSATION_NOT_FOUND);
    }
}
