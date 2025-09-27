package io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.Data;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DifyDocumentListResponse {
    private List<Data> data;
}
