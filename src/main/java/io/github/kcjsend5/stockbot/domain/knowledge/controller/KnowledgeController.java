package io.github.kcjsend5.stockbot.domain.knowledge.controller;

import io.github.kcjsend5.stockbot.global.api.difyProducts.service.DifyProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final DifyProductsService difyProductsService;

    @PostMapping("/set")
    public ResponseEntity<Void> setKnowledge() throws URISyntaxException {
        difyProductsService.knowledgeSave();
        return ResponseEntity.ok().build();
    };


}
