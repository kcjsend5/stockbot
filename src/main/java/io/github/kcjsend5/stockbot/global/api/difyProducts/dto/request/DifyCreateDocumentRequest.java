package io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.Process;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DifyCreateDocumentRequest {

    private String name;
    private String text;
    private String indexing_technique;
    private Process process_rule;


}
