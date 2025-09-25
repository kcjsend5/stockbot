package io.github.kcjsend5.stockbot.global.exception.uri;

import io.github.kcjsend5.stockbot.global.exception.CustomException;
import io.github.kcjsend5.stockbot.global.exception.ErrorCode;

public class InvalidUriException extends CustomException {
    public InvalidUriException() {
        super(ErrorCode.INVALID_URI);
    }
}
