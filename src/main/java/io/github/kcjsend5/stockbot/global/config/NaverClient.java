package io.github.kcjsend5.stockbot.global.config;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response.NaverNewsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name="NaverClient",url = "https://openapi.naver.com/v1/search/news.json")
public interface NaverClient {

    @GetMapping(consumes = APPLICATION_JSON_VALUE)
    NaverNewsResponse news(
            @RequestHeader("X-Naver-Client-Id") String clientId,
            @RequestHeader("X-Naver-Client-Secret") String clientSecret,
            @RequestParam("query") String query,
            @RequestParam("display") int display,
            @RequestParam("sort") String sort
    );


}
