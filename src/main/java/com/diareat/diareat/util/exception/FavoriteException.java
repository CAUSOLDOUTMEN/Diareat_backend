package com.diareat.diareat.util.exception;

import com.diareat.diareat.util.api.ResponseCode;

public class FavoriteException extends BaseException{
    public FavoriteException(ResponseCode responseCode) {
        super(responseCode);
    }
}
