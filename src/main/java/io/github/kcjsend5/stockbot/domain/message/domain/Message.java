package io.github.kcjsend5.stockbot.domain.message.domain;

import io.github.kcjsend5.stockbot.domain.conversation.domain.Conversation;
import io.github.kcjsend5.stockbot.global.entity.BaseEntity;
import io.github.kcjsend5.stockbot.type.Sender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="conversation_id")
    private Conversation conversation;

    private String messageText;

    @Enumerated(EnumType.STRING)
    private Sender sender;
}
