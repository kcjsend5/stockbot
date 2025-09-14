package io.github.kcjsend5.stockbot.global.exception.email;

import io.github.kcjsend5.stockbot.global.exception.CustomException;
import io.github.kcjsend5.stockbot.global.exception.ErrorCode;

public class InvalidEmailException extends CustomException {
    public InvalidEmailException() {
        super(ErrorCode.INVALID_EMAIL);
    }
}
