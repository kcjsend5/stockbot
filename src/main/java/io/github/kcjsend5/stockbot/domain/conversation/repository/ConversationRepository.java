package io.github.kcjsend5.stockbot.domain.conversation.repository;

import io.github.kcjsend5.stockbot.domain.conversation.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
}
