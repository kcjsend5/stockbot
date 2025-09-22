package io.github.kcjsend5.stockbot.global.config;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request.DifySendChatRequest;
import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.DifySendChatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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

}
