package io.github.kcjsend5.stockbot.global.exception.email;

import io.github.kcjsend5.stockbot.global.exception.CustomException;
import io.github.kcjsend5.stockbot.global.exception.ErrorCode;

public class DuplicateEmailException extends CustomException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
