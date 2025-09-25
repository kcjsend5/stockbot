package io.github.kcjsend5.stockbot.global.exception.user;

import io.github.kcjsend5.stockbot.global.exception.CustomException;
import io.github.kcjsend5.stockbot.global.exception.ErrorCode;

public class ForbiddenUserException extends CustomException {
    public ForbiddenUserException(){
        super(ErrorCode.FORBIDDEN_USER);
    }
}
