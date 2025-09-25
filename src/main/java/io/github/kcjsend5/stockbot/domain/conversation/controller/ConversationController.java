package io.github.kcjsend5.stockbot.domain.conversation.controller;

import io.github.kcjsend5.stockbot.domain.conversation.dto.request.ConversationMessageRequest;
import io.github.kcjsend5.stockbot.domain.conversation.dto.request.ConversationRequest;
import io.github.kcjsend5.stockbot.domain.conversation.dto.response.ConversationListResponse;
import io.github.kcjsend5.stockbot.domain.conversation.dto.response.ConversationMessageResponse;
import io.github.kcjsend5.stockbot.domain.conversation.dto.response.ConversationResponse;
import io.github.kcjsend5.stockbot.domain.conversation.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/create")
    public ResponseEntity<ConversationResponse> create(@RequestBody ConversationRequest request) throws URISyntaxException {
        return ResponseEntity.ok(conversationService.save(request));
    }

    @PostMapping("/send")
    public ResponseEntity<ConversationMessageResponse> send(@RequestBody ConversationMessageRequest request) throws URISyntaxException {
        return ResponseEntity.ok(conversationService.send(request));
    }

    @GetMapping("/list")
    public ResponseEntity<ConversationListResponse> getConversations(){
        return ResponseEntity.ok(conversationService.getConversations());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteConversation(@RequestParam String conversationId) throws URISyntaxException {
        conversationService.conversationDelete(conversationId);
        return ResponseEntity.ok().build();
    }

}
