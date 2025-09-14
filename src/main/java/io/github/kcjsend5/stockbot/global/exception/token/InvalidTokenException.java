package io.github.kcjsend5.stockbot.global.exception.token;

import io.github.kcjsend5.stockbot.global.exception.CustomException;
import io.github.kcjsend5.stockbot.global.exception.ErrorCode;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
