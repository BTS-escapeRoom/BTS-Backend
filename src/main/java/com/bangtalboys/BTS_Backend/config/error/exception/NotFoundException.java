package com.bangtalboys.BTS_Backend.config.error.exception;

import com.bangtalboys.BTS_Backend.config.error.ErrorCode;

public class NotFoundException extends BusinessBaseException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
