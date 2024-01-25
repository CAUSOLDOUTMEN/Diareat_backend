package com.diareat.diareat.util.exception;

import com.diareat.diareat.util.api.ResponseCode;

public class ValidException extends BaseException {
    public ValidException(ResponseCode responseCode) {
        super(responseCode);
    }
}
