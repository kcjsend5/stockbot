package io.github.kcjsend5.stockbot.domain.conversation.service;

import io.github.kcjsend5.stockbot.domain.conversation.domain.Conversation;
import io.github.kcjsend5.stockbot.domain.conversation.dto.request.ConversationRequest;
import io.github.kcjsend5.stockbot.domain.conversation.dto.response.ConversationResponse;
import io.github.kcjsend5.stockbot.domain.conversation.repository.ConversationRepository;
import io.github.kcjsend5.stockbot.domain.knowledge.domain.Knowledge;
import io.github.kcjsend5.stockbot.domain.knowledge.dto.response.KnowledgeResponse;
import io.github.kcjsend5.stockbot.domain.message.dto.response.MessageResponse;
import io.github.kcjsend5.stockbot.domain.user.domain.User;
import io.github.kcjsend5.stockbot.domain.user.repository.UserRepository;
import io.github.kcjsend5.stockbot.domain.user.service.UserService;
import io.github.kcjsend5.stockbot.global.exception.category.InvalidCategoryException;
import io.github.kcjsend5.stockbot.global.exception.email.InvalidEmailException;
import io.github.kcjsend5.stockbot.global.exception.user.UserNotFoundException;
import io.github.kcjsend5.stockbot.global.jwt.custom.CustomUserDetails;
import io.github.kcjsend5.stockbot.global.util.SecurityUtil;
import io.github.kcjsend5.stockbot.type.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ConversationResponse save(ConversationRequest request){

        Long userId =  SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Category category = request.getCategory();//미리 만들어 놓은 채팅방 ID를 통해 선택하기

        String conversationId = "";
        String knowledgeId;
        String chatFlowId;

        switch (category){
            case coin -> {chatFlowId = "448b7e8c-441e-4d65-b50a-90dd2ba291ea";knowledgeId = "69ab286f-4ffa-4457-85e6-f109581fa6b3";}
            case stock -> {chatFlowId = "465af60c-3c97-4784-8b48-305dcf6ef508";knowledgeId = "7905b43e-f01b-4da3-86e9-cdb98da174d0";}
            case bonds -> {chatFlowId = "10615d57-c081-43c5-813d-b3c109bb4fad";knowledgeId = "1dd2a824-21b1-43a7-b7d3-f2e5de4e1d52";}
            case estate -> {chatFlowId = "9d0bc910-63b5-47ab-9d27-dbea99b36efb";knowledgeId = "36c321f4-0b2a-411f-b487-a67210e448d5";}
            case futures -> {chatFlowId = "69072110-d49a-4b78-8fbb-8654e16e554f";knowledgeId = "4f5e04ee-9094-424b-8392-2b359541de33";}
            case commodities -> {chatFlowId = "a2df9879-4475-4473-a822-0cc5297f1854";knowledgeId = "7b15c364-a56a-4ee5-a915-3613059bb7fe";}
            default -> {throw new InvalidCategoryException();}
        }

        //DIFY 대화방 생성 API 호출, 각 대화방 id 추출

        Knowledge knowledge = Knowledge.builder()
                .knowledgeId(knowledgeId)
                .category(category)
                .build();

        Conversation conversation = Conversation.builder()
                .chatFlowId(chatFlowId)
                .conversationId(conversationId)
                .conversationName(request.getConversationName())
                .build();

        conversation.setKnowledge(knowledge);
        user.addConversation(conversation);

        conversationRepository.save(conversation);

        List<MessageResponse> messageResponses = conversation
                .getMessages()
                .stream()
                .map(m->new MessageResponse(m.getId(),m.getMessageText(),m.getSender()))
                .toList();

        KnowledgeResponse knowledgeResponse = new KnowledgeResponse(knowledge.getId(),knowledgeId,category);

        return new ConversationResponse(conversation.getId(),messageResponses,knowledgeResponse,conversationId,chatFlowId,request.getConversationName());
    }
}
