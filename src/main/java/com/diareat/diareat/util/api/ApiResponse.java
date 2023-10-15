package com.diareat.diareat.util.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private ApiHeader header;
    private T data;
    private String msg;

    private static final int SUCCESS = 200;

    private ApiResponse(ApiHeader header, T data, String msg) {
        this.header = header;
        this.data = data;
        this.msg = msg;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<T>(new ApiHeader(SUCCESS, "SUCCESS"), data, message);
    }

    public static <T> ApiResponse<T> fail(ResponseCode responseCode) {
        return new ApiResponse<T>(new ApiHeader(responseCode.getHttpStatusCode(), responseCode.getMessage()), null, responseCode.getMessage());
    }
}