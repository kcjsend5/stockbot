package io.github.kcjsend5.stockbot.global.exception.category;

import io.github.kcjsend5.stockbot.global.exception.CustomException;
import io.github.kcjsend5.stockbot.global.exception.ErrorCode;

public class InvalidCategoryException extends CustomException {
    public InvalidCategoryException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
