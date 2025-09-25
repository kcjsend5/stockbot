package io.github.kcjsend5.stockbot.global.api.difyProducts.dto.request;

import io.github.kcjsend5.stockbot.type.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class DifyProductsRequest {

    private Category category;
    private String email;
    private String query;
}
