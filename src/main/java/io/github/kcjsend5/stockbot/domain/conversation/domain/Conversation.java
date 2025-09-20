package io.github.kcjsend5.stockbot.domain.conversation.domain;

import io.github.kcjsend5.stockbot.domain.knowledge.domain.Knowledge;
import io.github.kcjsend5.stockbot.domain.message.domain.Message;
import io.github.kcjsend5.stockbot.domain.user.domain.User;
import io.github.kcjsend5.stockbot.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Conversation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "conversation",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "knowledge_id")
    private Knowledge knowledge;

    private String conversationName;

    private String conversationId;//대화방 ID

    private String chatFlowId;

    public void setKnowledge(Knowledge knowledge){
        this.knowledge = knowledge;
        knowledge.setConversation(this);
    }

    public void setUser(User user){
        this.user = user;
    }
}
