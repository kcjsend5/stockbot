package io.github.kcjsend5.stockbot.global.api.difyProducts.dto.response;

import io.github.kcjsend5.stockbot.global.api.difyProducts.dto.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverNewsResponse {

    private int total;
    private List<Item> items;

}
