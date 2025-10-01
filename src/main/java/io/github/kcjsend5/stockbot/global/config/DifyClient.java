package io.github.kcjsend5.stockbot.global.config;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyCreateDocumentRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifyDeleteRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifySendChatRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.DifyDocumentListResponse;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.DifySendChatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name="DifyClient",url = "USE_DYNAMIC_URI")
public interface DifyClient{

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    DifySendChatResponse sendChat(
            URI uri,
            @RequestHeader("Authorization") String apiKey,
            @RequestBody DifySendChatRequest request
    );

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE)
    void deleteChat(
            URI uri,
            @RequestHeader("Authorization") String apiKey,
            @RequestBody DifyDeleteRequest request
    );

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    void createDocument(
            URI uri,
            @RequestHeader("Authorization") String apiKey,
            @RequestBody DifyCreateDocumentRequest request
    );

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE)
    void deleteDocument(
            URI uri,
            @RequestHeader("Authorization") String apiKey
    );

    @GetMapping(consumes = APPLICATION_JSON_VALUE)
    DifyDocumentListResponse getDocument(
            URI uri,
            @RequestHeader("Authorization") String apiKey,
            @RequestParam("limit") int limit
    );
}
