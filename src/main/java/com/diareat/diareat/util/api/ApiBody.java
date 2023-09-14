package com.diareat.diareat.util.api;

public class ApiBody<T> {

    private final T data;
    private final T msg;

    public ApiBody(T data, T msg) {
        this.data = data;
        this.msg = msg;
    }

    public T getData() {
        return data;
    }
}
