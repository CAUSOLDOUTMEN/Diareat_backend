package com.diareat.diareat.util.exception;

import com.diareat.diareat.util.api.ResponseCode;

public class UserException extends BaseException {
    public UserException(ResponseCode responseCode) {
        super(responseCode);
    }
}
