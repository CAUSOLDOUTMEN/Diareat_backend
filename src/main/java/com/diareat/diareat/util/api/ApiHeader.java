package com.diareat.diareat.util.api;

public class ApiHeader {

    private int code;
    private String message;

    public ApiHeader(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiHeader() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
