package io.github.kcjsend5.stockbot.domain.user.domain;

import io.github.kcjsend5.stockbot.domain.conversation.domain.Conversation;
import io.github.kcjsend5.stockbot.global.entity.BaseEntity;
import io.github.kcjsend5.stockbot.type.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;//로그인 아이디

    @NotBlank
    private String password;

    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    List<Conversation> conversations = new ArrayList<>();

    public void addConversation(Conversation conversation) {
        this.conversations.add(conversation);
        conversation.setUser(this);
    }
}
