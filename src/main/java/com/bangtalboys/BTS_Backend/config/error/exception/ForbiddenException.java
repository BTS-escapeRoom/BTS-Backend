package com.bangtalboys.BTS_Backend.config.error.exception;

import com.bangtalboys.BTS_Backend.config.error.ErrorCode;

public class ForbiddenException extends BusinessBaseException{
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public ForbiddenException() {
        super(ErrorCode.METHOD_NOT_ALLOWED);
    }
}
