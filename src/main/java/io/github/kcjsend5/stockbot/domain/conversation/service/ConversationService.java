package io.github.kcjsend5.stockbot.domain.conversation.service;

import io.github.kcjsend5.stockbot.domain.conversation.domain.Conversation;
import io.github.kcjsend5.stockbot.domain.conversation.dto.request.ConversationMessageRequest;
import io.github.kcjsend5.stockbot.domain.conversation.dto.request.ConversationRequest;
import io.github.kcjsend5.stockbot.domain.conversation.dto.response.ConversationListResponse;
import io.github.kcjsend5.stockbot.domain.conversation.dto.response.ConversationMessageResponse;
import io.github.kcjsend5.stockbot.domain.conversation.dto.response.ConversationResponse;
import io.github.kcjsend5.stockbot.domain.conversation.repository.ConversationRepository;
import io.github.kcjsend5.stockbot.domain.knowledge.domain.Knowledge;
import io.github.kcjsend5.stockbot.domain.knowledge.dto.response.KnowledgeResponse;
import io.github.kcjsend5.stockbot.domain.message.domain.Message;
import io.github.kcjsend5.stockbot.domain.message.dto.response.MessageResponse;
import io.github.kcjsend5.stockbot.domain.user.domain.User;
import io.github.kcjsend5.stockbot.domain.user.repository.UserRepository;
import io.github.kcjsend5.stockbot.domain.user.service.UserService;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyProductsRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifySendChatRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.DifySendChatResponse;
import io.github.kcjsend5.stockbot.global.api.difyProducts.service.DifyProductsService;
import io.github.kcjsend5.stockbot.global.exception.category.InvalidCategoryException;
import io.github.kcjsend5.stockbot.global.exception.conversation.ConversationNotFoundException;
import io.github.kcjsend5.stockbot.global.exception.email.InvalidEmailException;
import io.github.kcjsend5.stockbot.global.exception.user.ForbiddenUserException;
import io.github.kcjsend5.stockbot.global.exception.user.UserNotFoundException;
import io.github.kcjsend5.stockbot.global.jwt.custom.CustomUserDetails;
import io.github.kcjsend5.stockbot.global.util.SecurityUtil;
import io.github.kcjsend5.stockbot.type.Category;
import io.github.kcjsend5.stockbot.type.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final DifyProductsService difyProductsService;

    @Transactional
    public ConversationResponse save(ConversationRequest request) throws URISyntaxException {

        Long userId =  SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Category category = request.getCategory();//미리 만들어 놓은 채팅방 ID를 통해 선택하기
        String knowledgeId = setKnowledgeId(category);

        //DIFY 대화방 생성 API 호출, 각 대화방 id 추출
        DifySendChatResponse difySendChatResponse =difyProductsService.sendChat(new DifyProductsRequest(category,user.getEmail(),"대화방 생성 중, 응답 거부"));

        String conversationId = difySendChatResponse.getConversation_id();

        Knowledge knowledge = Knowledge.builder()
                .knowledgeId(knowledgeId)
                .category(category)
                .build();

        Conversation conversation = Conversation.builder()
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

        return new ConversationResponse(conversation.getId(),messageResponses,knowledgeResponse,conversationId,request.getConversationName());
    }

    @Transactional
    public ConversationMessageResponse send(ConversationMessageRequest request) throws URISyntaxException {

        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Conversation conversation = conversationRepository.findByConversationId(request.getConversationId()).orElseThrow(ConversationNotFoundException::new);
        Category category = conversation.getKnowledge().getCategory();

        DifySendChatResponse difySendChatResponse = difyProductsService.sendChat(new DifyProductsRequest(category,user.getEmail(),request.getQuery()));

        Message userMessage = Message.builder()
                .messageText(request.getQuery())
                .sender(Sender.user)
                .build();

        Message aiMessage = Message.builder()
                .messageText(difySendChatResponse.getAnswer())
                .sender(Sender.AI)
                .build();

        conversation.addMessage(userMessage);
        conversation.addMessage(aiMessage);

        MessageResponse messageResponse = new MessageResponse(aiMessage.getId(),difySendChatResponse.getAnswer(),Sender.AI);

        return new ConversationMessageResponse(messageResponse,Sender.AI,request.getConversationId());

    }

    public ConversationListResponse getConversations(){

        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<String> conversationIds = user.getConversations()
                .stream()
                .map(Conversation::getConversationId)
                .toList();

        List<String> conversationNames = user.getConversations()
                .stream()
                .map(Conversation::getConversationName)
                .toList();

        return new ConversationListResponse(conversationIds,conversationNames);
    }

    @Transactional
    public void conversationDelete(String conversationId) throws URISyntaxException {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Conversation conversation = conversationRepository.findByConversationId(conversationId).orElseThrow(ConversationNotFoundException::new);
        if(Objects.equals(conversation.getUser().getId(), user.getId())){
            throw new ForbiddenUserException();
        }
        difyProductsService.deleteChat(user.getEmail(), conversationId);
        user.getConversations().remove(conversation);
    }

    private String setKnowledgeId(Category category) {

        switch (category){
            case coin -> {return "69ab286f-4ffa-4457-85e6-f109581fa6b3";}
            case stock -> {return "7905b43e-f01b-4da3-86e9-cdb98da174d0";}
            case bonds -> {return "1dd2a824-21b1-43a7-b7d3-f2e5de4e1d52";}
            case estate -> {return "36c321f4-0b2a-411f-b487-a67210e448d5";}
            case futures -> {return "4f5e04ee-9094-424b-8392-2b359541de33";}
            case commodities -> {return "7b15c364-a56a-4ee5-a915-3613059bb7fe";}
            default -> {throw new InvalidCategoryException();}
        }
    }
}
