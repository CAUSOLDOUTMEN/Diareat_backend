package com.diareat.diareat.util.exception;

import com.diareat.diareat.util.api.ResponseCode;

public class FoodException extends BaseException {
    public FoodException(ResponseCode responseCode) {
        super(responseCode);
    }
}
