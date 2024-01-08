package com.diareat.diareat.util.exception;

import com.diareat.diareat.util.api.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException {

    private final ResponseCode responseCode;

    @Override
    public String getMessage() {
        return responseCode.getMessage();
    }
}
