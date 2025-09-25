package io.github.kcjsend5.stockbot.global.api.difyProducts.service;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyDeleteRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyProductsRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifySendChatRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.DifySendChatResponse;
import io.github.kcjsend5.stockbot.global.config.DifyClient;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DifyProductsService {

    private final DifyClient difyClient;

    @Value("${dify.chat.secret}")
    private String apiKey;
    @Value("${dify.uri}")
    private String basicUri;

    public DifySendChatResponse sendChat(DifyProductsRequest request) throws URISyntaxException {

        DifySendChatRequest difySendChatRequest = DifySendChatRequest.builder()
                .user(request.getEmail())
                .inputs(Map.of("category",request.getCategory().toString()))
                .response_mode("blocking")
                .query(request.getQuery())
                .build();
        // 파일 입력 기능 만들기
        return difyClient.sendChat(new URI(basicUri+"/chat-messages"),apiKey,difySendChatRequest);
    }

    public void deleteChat(String email,String conversationId) throws URISyntaxException {
        DifyDeleteRequest request = DifyDeleteRequest.builder()
                .user(email)
                .build();
        difyClient.deleteChat(new URI(basicUri+"/conversations/"+conversationId),apiKey,request);
    }

}
