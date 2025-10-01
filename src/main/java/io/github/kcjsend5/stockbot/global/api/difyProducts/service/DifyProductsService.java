package io.github.kcjsend5.stockbot.global.api.difyProducts.service;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.Data;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.Item;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.Process;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyCreateDocumentRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyDeleteRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyProductsRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifySendChatRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.DifyDocumentListResponse;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.DifySendChatResponse;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.NaverNewsResponse;
import io.github.kcjsend5.stockbot.global.config.DifyClient;
import io.github.kcjsend5.stockbot.global.config.NaverClient;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DifyProductsService {

    private final DifyClient difyClient;
    private final NaverClient naverClient;

    @Value("${dify.chat.secret}")
    private String apiKey;
    @Value("${dify.knowledge.secret}")
    private String knowledgeKey;
    @Value("${dify.uri}")
    private String basicUri;
    @Value("${naver.id}")
    private String naverId;
    @Value("${naver.secret}")
    private String naverSecret;

    @Value("${dify.knowledge.id.coin}") private String coinId;
    @Value("${dify.knowledge.id.stock}") private String stockId;
    @Value("${dify.knowledge.id.bonds}") private String bondsId;
    @Value("${dify.knowledge.id.estate}") private String estateId;
    @Value("${dify.knowledge.id.futures}") private String futuresId;
    @Value("${dify.knowledge.id.commodities}") private String commoditiesId;

    public DifySendChatResponse sendChat(DifyProductsRequest request) throws URISyntaxException {

        DifySendChatRequest difySendChatRequest = DifySendChatRequest.builder()
                .user(request.getEmail())
                .inputs(Map.of("category",request.getCategory().toString()))
                .response_mode("blocking")
                .query(request.getQuery())
                .build();
        // 파일 입력 기능 만들기
        return difyClient.sendChat(new URI(basicUri+"/chat-messages"),"Bearer "+apiKey,difySendChatRequest);
    }

    public void deleteChat(String email,String conversationId) throws URISyntaxException {
        DifyDeleteRequest request = DifyDeleteRequest.builder()
                .user(email)
                .build();
        difyClient.deleteChat(new URI(basicUri+"/conversations/"+conversationId),"Bearer "+apiKey,request);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void knowledgeSave() throws URISyntaxException {

        List<String> ids = List.of(stockId,coinId,bondsId,estateId,futuresId,commoditiesId);

        NaverNewsResponse stock =naverClient.news(naverId, naverSecret, URLEncoder.encode("주식", StandardCharsets.UTF_8), 100, "date");
        NaverNewsResponse coin =naverClient.news(naverId, naverSecret, URLEncoder.encode("암호화폐", StandardCharsets.UTF_8), 100, "date");
        NaverNewsResponse futures =naverClient.news(naverId, naverSecret, URLEncoder.encode("선물", StandardCharsets.UTF_8), 100, "date");
        NaverNewsResponse bonds =naverClient.news(naverId, naverSecret, URLEncoder.encode("채권", StandardCharsets.UTF_8), 100, "date");
        NaverNewsResponse commodities =naverClient.news(naverId, naverSecret, URLEncoder.encode("현물", StandardCharsets.UTF_8), 100, "date");
        NaverNewsResponse estate =naverClient.news(naverId, naverSecret, URLEncoder.encode("부동산", StandardCharsets.UTF_8), 100, "date");

        List<NaverNewsResponse> news = List.of(stock,coin,bonds,estate,futures,commodities);

        Process process = new Process("automatic");

        for(int i = 0; i<ids.size();i++){
            DifyDocumentListResponse documentListResponse = difyClient.getDocument(new URI(basicUri+"/datasets/"+ids.get(i)+"/documents"),"Bearer "+knowledgeKey);
            List<Data> dataList = documentListResponse.getData();
            for(Data data:dataList){
                difyClient.deleteDocument(new URI(basicUri+"/datasets/"+ids.get(i)+"/documents/"+data.getId()), "Bearer "+knowledgeKey);
            }
            for(Item item: news.get(i).getItems()){
                DifyCreateDocumentRequest documentRequest = DifyCreateDocumentRequest.builder()
                        .name(item.getTitle())
                        .text(item.getDescription())
                        .indexing_technique("high_quality")
                        .process_rule(process)
                        .build();
                difyClient.createDocument(new URI(basicUri+"/datasets/"+ids.get(i)+"/document/create-by-text"),"Bearer "+knowledgeKey,documentRequest);
            }
        }

    }

}
