package io.github.kcjsend5.stockbot.global.api.difyProducts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String title;
    private String originallink;
    private String link;
    private String description;
}
