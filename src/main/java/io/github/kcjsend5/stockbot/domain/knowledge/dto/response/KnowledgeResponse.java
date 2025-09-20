package io.github.kcjsend5.stockbot.domain.knowledge.dto.response;

import io.github.kcjsend5.stockbot.type.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeResponse {

    private Long id;
    private String knowledgeId;
    private Category subject;

}
