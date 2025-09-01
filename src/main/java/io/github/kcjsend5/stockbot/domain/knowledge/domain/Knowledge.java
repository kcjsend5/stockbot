package io.github.kcjsend5.stockbot.domain.knowledge.domain;

import io.github.kcjsend5.stockbot.domain.conversation.domain.Conversation;
import io.github.kcjsend5.stockbot.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Knowledge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "knowledge",cascade = CascadeType.ALL)
    private Conversation conversation;

    private String knowledgeId;
}
