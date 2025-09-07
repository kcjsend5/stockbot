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

    private String subject;//주제에 따라 api로 뉴스를 크롤링 후 지식에 청크를 입력
}
